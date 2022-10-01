package net.gestalt.roblox.payloads;

import com.google.gson.annotations.Expose;
import lombok.Getter;

public interface GeneralPayloads {
    @Getter
    class EndpointError {
        @Getter
        public static class Error {
            @Expose
            int code;
            @Expose
            String message;
            @Expose
            String userFacingMessage;
        }

        @Expose
        private Error[] errors;

        public EndpointError() {}
    }
}