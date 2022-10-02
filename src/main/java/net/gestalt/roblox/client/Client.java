package net.gestalt.roblox.client;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import net.gestalt.exceptions.InvalidAccountIdException;
import net.gestalt.exceptions.InvalidAccountNameException;
import net.gestalt.exceptions.InvalidCookieException;
import net.gestalt.http.OkRobloxClient;
import net.gestalt.roblox.accounts.Account;
import net.gestalt.roblox.payloads.AccountPayloads;
import net.gestalt.utils.ExcludeFromJacocoGeneratedReport;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import reactor.core.publisher.Mono;

@Getter
@Setter
public class Client {
    private final static Gson GSON = new Gson();
    private final OkRobloxClient okRobloxClient;

    public Client() {
        this.okRobloxClient = new OkRobloxClient();
    }

    /**
     * This method will retrieve the provided user.
     * @param accountName The username of the account.
     */
    public Mono<Account> getAccountFromUsername(String accountName) {
        // Pre-request.
        // We'll set up the payload here.
        AccountPayloads.GetAccountsByNamePayload payload = new AccountPayloads.GetAccountsByNamePayload();
        payload.setUsernames(new String[]{ accountName });

        Request request = new Request.Builder()
                .url("https://users.roblox.com/v1/usernames/users")
                .post(RequestBody.create(GSON.toJson(payload), MediaType.parse("application/json")))
                .build();

        // We will have to map the multiple username's into one account.
        // If it errors, return an invalid account name exception.
        return this.okRobloxClient.execute(request, AccountPayloads.AccountNamePayload.class)
                .map(accountNamePayload -> {
                    AccountPayloads.AccountNamePayload.Data[] data = accountNamePayload.getData();
                    return data[0];
                })
                .onErrorResume(e -> Mono.error(new InvalidAccountNameException())) // Invalid username.
                .map(AccountPayloads.AccountNamePayload.Data::getId)
                .flatMap(this::getAccount);
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

        // Contains a mono that'll return an account.
        // Map the payload to an actual account.
        return this.okRobloxClient.execute(request, AccountPayloads.AccountPayload.class)
                .onErrorResume(e -> Mono.error(new InvalidAccountIdException()))
                .map(accountPayload -> Account.fromData(accountPayload, this.okRobloxClient));
    }

    /**
     * This method will retrieve details about the currently authenticated account.
     * @return The account object belonging to the authenticated user.
     */
    public Mono<Account> getAuthenticatedAccount() {
        Request request = new Request.Builder()
                .url("https://users.roblox.com/v1/users/authenticated")
                .build();

        // Map the authenticated user to an account object.
        return this.okRobloxClient.execute(request, AccountPayloads.AuthenticatedAccount.class, true)
                .onErrorResume(e -> Mono.error(new InvalidCookieException())) // The invalid cookie is the issue.
                .flatMap(account -> this.getAccount(account.getId()));
    }

    /**
     * This method will set the cookie field.
     * @param cookie The Roblox cookie.
     */
    @ExcludeFromJacocoGeneratedReport
    public void setCookie(String cookie) throws InvalidCookieException {
        this.okRobloxClient.setCookie(cookie);
    }
}