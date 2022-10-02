package net.gestalt.roblox.accounts;

import net.gestalt.http.OkRobloxClient;
import net.gestalt.roblox.payloads.AccountPayloads;
import okhttp3.Request;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;

public record Account(OkRobloxClient okRobloxClient, String description, String date, String externalAppDisplayName,
                      String name, String displayName, boolean isBanned, boolean hasVerifiedBadge, long id) {
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