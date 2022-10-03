package net.gestalt.roblox.groups;

import lombok.Getter;

@Getter
public final class Owner {
    private final boolean hasVerifiedBadge;
    private final long userId;
    private final String username;
    private final String displayName;

    public Owner(boolean hasVerifiedBadge, long userId, String username, String displayName) {
        this.hasVerifiedBadge = hasVerifiedBadge;
        this.userId = userId;
        this.username = username;
        this.displayName = displayName;
    }
}