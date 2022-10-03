package net.gestalt.roblox.payloads;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import net.gestalt.roblox.groups.Owner;
import net.gestalt.roblox.groups.Shout;

public interface GroupPayloads {
    @Getter
    class GetGroupPayload {
        @Expose
        private long id;
        @Expose
        private String name, description;
        @Expose
        private int memberCount;
        @Expose
        private boolean isBuildersClubOnly, publicEntryAllowed, hasVerifiedBadge;
        @Expose
        private Owner owner;
        @Expose
        private Shout shout;
    }
}