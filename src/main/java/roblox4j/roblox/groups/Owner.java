package roblox4j.roblox.groups;

import lombok.Getter;

@Getter
public final class Owner {
    private boolean hasVerifiedBadge;
    private long userId;
    private String username;
    private String displayName;
}