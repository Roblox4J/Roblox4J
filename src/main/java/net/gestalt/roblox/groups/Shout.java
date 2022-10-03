package net.gestalt.roblox.groups;

import com.google.gson.annotations.Expose;
import lombok.Getter;

@Getter
public final class Shout {
    @Expose
    private final String body;
    @Expose
    private final String created;
    @Expose
    private final String updated;
    @Expose
    private final Owner poster;

    public Shout(String body, String created, String updated, Owner poster) {
        this.body = body;
        this.created = created;
        this.updated = updated;
        this.poster = poster;
    }
}