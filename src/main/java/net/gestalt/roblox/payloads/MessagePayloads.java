package net.gestalt.roblox.payloads;

import com.google.gson.annotations.Expose;
import lombok.Getter;

public interface MessagePayloads {
    @Getter
    class CanMessagePayload {
        @Expose
        boolean canMessage;
    }
}