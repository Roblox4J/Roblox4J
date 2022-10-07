package roblox4j.exceptions;

import lombok.Getter;
import roblox4j.roblox.payloads.GeneralPayloads;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Getter
public class InvalidRequestException extends Exception {
    private final int code;
    private final String message;
    private final String userFacingMessage;

    public InvalidRequestException(int code, String message, String userFacingMessage) {
        super(message);
        this.code = code;
        this.message = message;
        this.userFacingMessage = userFacingMessage;
    }

    @Contract("_ -> new")
    public static @NotNull InvalidRequestException fromData(GeneralPayloads.EndpointError.@NotNull Error endpointError) {
        return new InvalidRequestException(endpointError.getCode(), endpointError.getMessage(),
                endpointError.getUserFacingMessage());
    }
}