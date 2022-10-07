package net.gestalt.roblox.client;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import net.gestalt.exceptions.InvalidAccountNameException;
import net.gestalt.exceptions.InvalidCookieException;
import net.gestalt.exceptions.InvalidIdException;
import net.gestalt.exceptions.InvalidRequestException;
import net.gestalt.http.OkRobloxClient;
import net.gestalt.roblox.accounts.Account;
import net.gestalt.roblox.games.Game;
import net.gestalt.roblox.groups.Group;
import net.gestalt.roblox.models.Model;
import net.gestalt.roblox.payloads.AccountPayloads;
import net.gestalt.roblox.payloads.GamePayloads;
import net.gestalt.roblox.payloads.GroupPayloads;
import net.gestalt.roblox.payloads.ModelPayloads;
import net.gestalt.utils.ExcludeFromJacocoGeneratedReport;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicLong;

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
                .onErrorResume(e -> Mono.error(InvalidAccountNameException::new)) // Invalid username.
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
                .onErrorResume(InvalidRequestException.class, e -> {
                    if (e.getCode() == 3)
                        return Mono.error(InvalidIdException::new);
                    return Mono.error(e);
                })
                .map(accountPayload -> Account.fromData(accountPayload, this.okRobloxClient));
    }


    /**
     * This method will fetch the provided group.
     * @param id The id of the group
     * @return A mono object containing the group.
     */
    @SuppressWarnings("unused")
    @ExcludeFromJacocoGeneratedReport
    public Mono<Group> getGroup(long id) {
        return this.getGroup(String.valueOf(id));
    }

    /**
     * This method will fetch a group.
     * @param id The id belonging to the group.
     * @return A mono containing the group.
     */
    public Mono<Group> getGroup(String id) {
        Request request = new Request.Builder()
                .url("https://groups.roblox.com/v1/groups/%s".formatted(id))
                .build();

        return this.okRobloxClient.execute(request, GroupPayloads.GetGroupPayload.class)
                .onErrorResume(InvalidRequestException.class, e -> {
                    if (e.getCode() == 1)
                        return Mono.error(InvalidIdException::new);
                    return Mono.error(e);
                })
                .map(groupPayload -> Group.fromData(groupPayload, this.okRobloxClient));
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
                .onErrorResume(e -> Mono.error(InvalidCookieException::new)) // The invalid cookie is the issue.
                .flatMap(account -> this.getAccount(account.getId()));
    }

    /**
     * This method will fetch the provided game.
     * @param id The id of the game.
     * @return The game mono object.
     */
    @SuppressWarnings("unused")
    @ExcludeFromJacocoGeneratedReport
    public Mono<Game> getGame(long id) {
        return this.getGame(String.valueOf(id));
    }

    /**
     * This method will fetch the provided game id.
     * @param id The id belonging to the game.
     * @return The game mono object.
     */
    public Mono<Game> getGame(String id) {
        // This will contain two requests.
        // One request to obtain the universe id and one for the general game payload.
        Request getUniverseIdRequest = new Request.Builder()
                .url("https://api.roblox.com/universes/get-universe-containing-place?placeid=%s".formatted(id))
                .build();

        AtomicLong universeId = new AtomicLong();

        //noinspection SwitchStatementWithTooFewBranches
        return this.okRobloxClient.execute(getUniverseIdRequest, GamePayloads.UniverseContainingPlacePayload.class)
                .onErrorResume(InvalidRequestException.class, e -> switch (e.getCode()) {
                    case 400 -> Mono.error(InvalidIdException::new);
                    default -> Mono.error(e);
                })
                .flatMap(universe -> {
                    universeId.set(universe.getUniverseId());
                    Request request = new Request.Builder()
                            .url("https://games.roblox.com/v1/games?universeIds=%s".formatted(universeId))
                            .build();
                    return this.okRobloxClient.execute(request, GamePayloads.GetUniversesPayload.class);
                })
                .map(universePayload -> Game.fromData(universePayload.getData()[0], universeId.get(),
                        this.okRobloxClient))
                .onErrorResume(ArrayIndexOutOfBoundsException.class, e -> Mono.error(InvalidIdException::new));
    }

    /**
     * This method will fetch a model from its id.
     * @param id The id of the model
     * @return The model mono object.
     */
    @SuppressWarnings("unused")
    @ExcludeFromJacocoGeneratedReport
    public Mono<Model> getModel(long id) {
        return this.getModel(String.valueOf(id));
    }

    /**
     * This method will fetch a model.
     * @param id The id of the model
     * @return The model mono object.
     */
    public Mono<Model> getModel(String id) {
        Request request = new Request.Builder()
                .url("https://economy.roblox.com/v2/assets/%s/details".formatted(id))
                .build();

        //noinspection SwitchStatementWithTooFewBranches
        return this.okRobloxClient.execute(request, ModelPayloads.GetInfoPayload.class)
                .onErrorResume(InvalidRequestException.class, e -> switch (e.getCode()) {
                    case 5 -> Mono.error(InvalidIdException::new);
                    default -> Mono.error(e);
                })
                .map(infoPayload -> Model.fromData(infoPayload, this.okRobloxClient));
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