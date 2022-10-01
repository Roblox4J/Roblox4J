package net.gestalt.roblox.client;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import net.gestalt.exceptions.InvalidAccountIdException;
import net.gestalt.exceptions.InvalidAccountNameException;
import net.gestalt.exceptions.InvalidCookieException;
import net.gestalt.exceptions.InvalidRequestException;
import net.gestalt.http.OkRobloxClient;
import net.gestalt.roblox.accounts.Account;
import net.gestalt.roblox.payloads.AccountPayloads;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import reactor.core.publisher.Mono;

@Getter
@Setter
public class Client {
    private final static Gson GSON = new Gson();
    private final OkRobloxClient okRobloxClient;

    public Client(String cookie) throws InvalidCookieException {
        this.okRobloxClient = new OkRobloxClient(cookie);
    }
    public Client() {
        this.okRobloxClient = new OkRobloxClient();
    }

    /**
     * This method will retrieve the provided user.
     * @param accountName The username of the account.
     */
    public Mono<Account> getAccountFromUsername(String accountName) {
        AccountPayloads.GetAccountsByNamePayload payload = new AccountPayloads.GetAccountsByNamePayload();
        payload.setUsernames(new String[]{ accountName });
        Request request = new Request.Builder()
                .url("https://users.roblox.com/v1/usernames/users")
                .post(RequestBody.create(GSON.toJson(payload), MediaType.parse("application/json")))
                .build();
        return Mono.create(sink -> this.okRobloxClient.execute(request, AccountPayloads.AccountNamePayload.class)
                .doOnError(sink::error)
                .map(r -> {
                    AccountPayloads.AccountNamePayload.Data[] data = r.getData();
                    // Make sure there's data.
                    if (data.length == 0)
                        sink.error(new InvalidAccountNameException());
                    return data[0].getId();
                })
                .flatMap(this::getAccount)
                .doOnSuccess(sink::success)
                .block());
    }

    /**
     * This method will retrieve the provided user.
     * @param id The corresponding id.
     */
    public Mono<Account> getAccount(long id) {
        return this.getAccount(String.valueOf(id));
    }

    /**
     * This method will retrieve the provided user.
     * @param id The corresponding id.
     */
    public Mono<Account> getAccount(String id) {
        Request request = new Request.Builder()
                .url("https://users.roblox.com/v1/users/%s".formatted(id))
                .build();
        return Mono.create(sink -> this.okRobloxClient.execute(request, AccountPayloads.AccountPayload.class)
                .doOnError(InvalidRequestException.class, r ->
                        sink.error(new InvalidAccountIdException()))
                .doOnError(sink::error)
                .map(r -> Account.fromData(r, this.okRobloxClient))
                .doOnSuccess(sink::success)
                .block());
    }

    /**
     * This method will retrieve details about the currently authenticated account.
     * @return The account object belonging to the authenticated user.
     */
    public Mono<Account> getAuthenticatedAccount() {
        Request request = new Request.Builder()
                .url("https://users.roblox.com/v1/users/authenticated")
                .build();
        return Mono.create(sink -> {
            try {
                this.okRobloxClient.execute(request, AccountPayloads.AuthenticatedAccount.class,
                                true)
                        .doOnError(sink::error)
                        .flatMap(r -> this.getAccount(r.getId()))
                        .doOnSuccess(sink::success)
                        .block();
            } catch (InvalidCookieException e) {
                sink.error(e);
            }
        });
    }

    /**
     * This method will set the cookie field.
     * @param cookie The Roblox cookie.
     */
    public void setCookie(String cookie) throws InvalidCookieException {
        this.okRobloxClient.setCookie(cookie);
    }
}