package net.gestalt.roblox.payloads;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

public interface AccountPayloads {
    @Getter
    class AccountPayload {
        @Expose
        private String description, date, externalAppDisplayName, name, displayName;
        @Expose
        boolean isBanned, hasVerifiedBadge;
        @Expose
        long id;
    }
    @Getter
    @Setter
    class GetAccountsByNamePayload {
        @Expose
        private String[] usernames;
        @Expose
        private boolean excludeBannedUsers;
    }
    @Getter
    class AccountNamePayload {
        @Getter
        public static class Data {
            @Expose
            private String requestedUsername;
            @Expose
            private boolean hasVerifiedBadge;
            @Expose
            private long id;
            @Expose
            private String name;
            @Expose
            private String displayName;
        }
        @Expose
        Data[] data;
    }
    @Getter
    class AuthenticatedAccount {
        @Expose
        long id;
        @Expose
        String name, displayName;
    }
}