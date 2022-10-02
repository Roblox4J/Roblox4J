package net.gestalt.roblox.accounts;

import lombok.extern.java.Log;
import net.gestalt.http.OkRobloxClient;
import net.gestalt.roblox.payloads.AccountPayloads;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Log
public record Account(OkRobloxClient okRobloxClient, String description, String date, String externalAppDisplayName,
                      String name, String displayName, boolean isBanned, boolean hasVerifiedBadge, long id) {

    /**
     * This method will load a Roblox account from an account payload.
     *
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