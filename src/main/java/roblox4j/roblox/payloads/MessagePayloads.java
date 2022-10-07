package roblox4j.roblox.payloads;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

public interface MessagePayloads {
    @Getter
    class CanMessagePayload {
        @Expose
        private boolean canMessage;
    }
    @Getter
    @Setter
    class SendMessagePayload {
        @Expose
        private String subject, body;
        @Expose
        private long recipientId;
    }
}