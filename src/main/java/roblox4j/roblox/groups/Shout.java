package roblox4j.roblox.groups;

import com.google.gson.annotations.Expose;
import lombok.Getter;

@Getter
public final class Shout {
    @Expose
    private String body;
    @Expose
    private String created;
    @Expose
    private String updated;
    @Expose
    private Owner poster;

}