package roblox4j.roblox.payloads;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import roblox4j.roblox.groups.Owner;
import roblox4j.roblox.groups.Shout;

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
        private boolean isBuildersClubOnly, publicEntryAllowed, hasVerifiedBadge, isLocked;
        @Expose
        private Owner owner;
        @Expose
        private Shout shout;
    }
    @Getter
    @Setter
    class SetNamePayload {
        @Expose
        private String name;
    }
    @Getter
    @Setter
    class SetDescriptionPayload {
        @Expose
        private String description;
    }
    @Getter
    @Setter
    class SendShoutPayload {
        @Expose
        private String message;
    }
    @Getter
    @Setter
    class SetOwnerPayload {
        @Expose
        private long userId;
    }
    @Getter
    @Setter
    class PayAccountPayload {
        @Getter
        @Setter
        public static class Recipient {
            @Expose
            private long recipientId;
            @Expose
            private String recipientType;
            @Expose
            private int amount;
        }
        @Expose
        @SerializedName("PayoutType")
        private String payoutType;
        @Expose
        @SerializedName("Recipients")
        private Recipient[] recipients;
    }
}