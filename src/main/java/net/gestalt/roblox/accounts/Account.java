package net.gestalt.roblox.accounts;

import net.gestalt.http.OkRobloxClient;
import net.gestalt.roblox.payloads.AccountPayloads;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@lombok.Getter
public class Account extends AccountPayloads.AccountPayload {
    private final OkRobloxClient okRobloxClient;
    private final String description;
    private final String date;
    private final String externalAppDisplayName;
    private final String name;
    private final String displayName;
    private final boolean isBanned;
    private final boolean hasVerifiedBadge;
    private final long id;

    public Account(OkRobloxClient okRobloxClient, String description, String date, String externalAppDisplayName,
                   String name, String displayName, boolean isBanned, boolean hasVerifiedBadge, long id) {
        this.okRobloxClient = okRobloxClient;
        this.description = description;
        this.date = date;
        this.externalAppDisplayName = externalAppDisplayName;
        this.name = name;
        this.displayName = displayName;
        this.isBanned = isBanned;
        this.hasVerifiedBadge = hasVerifiedBadge;
        this.id = id;
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