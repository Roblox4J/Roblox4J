package net.gestalt.roblox.accounts;

import net.gestalt.exceptions.InvalidCookieException;
import net.gestalt.http.OkRobloxClient;
import net.gestalt.roblox.payloads.AccountPayloads;
import net.gestalt.roblox.payloads.FriendPayloads;
import net.gestalt.roblox.payloads.MessagePayloads;
import okhttp3.Request;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public record Account(OkRobloxClient okRobloxClient, String description, String date, String externalAppDisplayName,
                      String name, String displayName, boolean isBanned, boolean hasVerifiedBadge, long id) {
    /**
     * This method will fetch a list of the accounts previous usernames.
     * @return A flux object that'll contain name objects.
     */
    public @NotNull Flux<AccountPayloads.UsernameHistoryPayload.Data> getUsernameHistory() {
        // Set up the request.
        // Leave cursor field blank.
        Request request = new Request.Builder()
                .url("https://users.roblox.com/v1/users/%s/username-history?limit=100&cursor=&sortOrder=Asc"
                        .formatted(this.id))
                .build();

        return this.okRobloxClient.executeMultiCursor(request, AccountPayloads.UsernameHistoryPayload.class)
                .flatMap(payload -> Flux.just(payload.getData()));
    }

    /**
     * This method will fetch all the account's friends.
     * @return A mono containing friends.
     */
    public @NotNull Mono<FriendPayloads.GetFriendsPayload.Data[]> getFriends() {
        Request request = new Request.Builder()
                .url("https://friends.roblox.com/v1/users/%s/friends".formatted(this.id))
                .build();

        return this.okRobloxClient.execute(request, FriendPayloads.GetFriendsPayload.class)
                .map(FriendPayloads.GetFriendsPayload::getData);
    }

    /**
     * This method will check if we can message the person.
     * @return Whether the authenticated user can message this account or not.
     */
    public @NotNull Mono<Boolean> canMessage() {
        Request request = new Request.Builder()
                .url("https://privatemessages.roblox.com/v1/messages/25/can-message")
                .build();

        return this.okRobloxClient.execute(request, MessagePayloads.CanMessagePayload.class, false)
                .onErrorResume(e -> Mono.error(InvalidCookieException::new))
                .map(MessagePayloads.CanMessagePayload::isCanMessage);
    }

    /**
     * This method will load a Roblox account from an account payload.
     * @param accountPayload The corresponding account payload.
     * @param okRobloxClient The HTTP client used to make requests.
     * @return The account object.
     */
    @Contract("_, _ -> new")
    public static @NotNull Account fromData(AccountPayloads.@NotNull AccountPayload accountPayload,
                                            OkRobloxClient okRobloxClient) {
        return new Account(okRobloxClient, accountPayload.getDescription(), accountPayload.getDate(),
                accountPayload.getExternalAppDisplayName(), accountPayload.getName(),
                accountPayload.getDisplayName(), accountPayload.isBanned(),
                accountPayload.isHasVerifiedBadge(), accountPayload.getId());
    }
}