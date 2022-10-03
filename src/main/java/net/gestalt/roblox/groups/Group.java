package net.gestalt.roblox.groups;

import net.gestalt.http.OkRobloxClient;
import net.gestalt.roblox.payloads.GroupPayloads;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Group(long id, String name, String description, Owner owner, Shout shout,
                    OkRobloxClient okRobloxClient) {

    /**
     * This method will load a group object from a payload.
     * @param groupPayload The payload.
     * @param okRobloxClient The Roblox HTTP client used to receive the payload.
     * @return The corresponding group object.
     */
    @Contract("_, _ -> new")
    public static @NotNull Group fromData(GroupPayloads.@NotNull GetGroupPayload groupPayload,
                                          OkRobloxClient okRobloxClient) {
        return new Group(groupPayload.getId(), groupPayload.getName(), groupPayload.getDescription(),
                groupPayload.getOwner(), groupPayload.getShout(), okRobloxClient);
    }
}