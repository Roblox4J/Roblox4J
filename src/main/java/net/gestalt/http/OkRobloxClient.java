package net.gestalt.http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.gestalt.exceptions.InvalidCookieException;
import net.gestalt.exceptions.InvalidRequestException;
import net.gestalt.roblox.payloads.GeneralPayloads;
import net.gestalt.utils.ExcludeFromJacocoGeneratedReport;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.io.IOException;

public class OkRobloxClient extends OkHttpClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(OkRobloxClient.class);
    private static final Gson GSON = new Gson();
    private static final String COOKIE_SIGNATURE = "_|WARNING:-DO-NOT-SHARE-THIS.--Sharing-this-will-allow-someone-to" +
            "-log-in-as-you-and-to-steal-your-ROBUX-and-items.|_";

    private String cookie;

    /**
     * Creates a Roblox client.
     */
    public OkRobloxClient() {
        super();
    }

    /**
     * This method will execute a OkHttpClient call using the reactor core.
     * @param request The corresponding request.
     * @param t       The response class.
     * @param <T>     The class that the response will be unmarshalled to.
     * @return The response object.
     */
    public <T> Mono<T> execute(Request request, Class<T> t) {
        return Mono.create(sink -> super.newCall(request).enqueue(new Callback() {
            @Override
            @ExcludeFromJacocoGeneratedReport
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                sink.error(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();

                try {
                    try {
                        // Try casting the body to the endpoint exception class.
                        GeneralPayloads.EndpointError error = GSON.fromJson(body, GeneralPayloads.EndpointError.class);
                        sink.error(InvalidRequestException.fromData(error.getErrors()[0]));
                    } catch (Exception ignored) {}

                    T serial = GSON.fromJson(body, t);
                    sink.success(serial);
                } catch (JsonSyntaxException e) {
                    sink.error(e);
                } finally {
                    // Silently close the response.
                    closeQuietly(response);
                }
            }
        }));
    }

    /**
     * This method will automatically add the cross-reference token header.
     * @param request The corresponding request.
     * @param t       The response class.
     * @param auth    Whether the request requires a cross-reference token.
     * @param <T>     The class that the response will be unmarshalled to.
     * @return The response object.
     */
    public <T> Mono<T> execute(@NotNull Request request, Class<T> t, boolean auth) {
        return this.getCrossReference()
                .mapNotNull(token -> {
                    // If authentication is required, we have to add the corresponding headers.
                    if (auth)
                        return request.newBuilder()
                                .addHeader("Cookie", ".ROBLOSECURITY=%s".formatted(this.cookie))
                                .addHeader("X-CSRF-TOKEN", token)
                                .build();

                    // Otherwise, return the request.
                    return request;
                })
                .flatMap(request1 -> this.execute(request1, t));
    }

    /**
     * This method will quietly close the provided response.
     * @param response The response.
     */
    @ExcludeFromJacocoGeneratedReport
    private void closeQuietly(@NotNull Response response) {
        try {
            response.body();
            response.close();
        } catch (Exception e) {
            LOGGER.warn("Failed to silently close response.", e);
        }
    }

    /**
     * This method will fetch a cross-reference token for the session.
     * @return A mono object containing the token.
     */
    private @NotNull Mono<String> getCrossReference() {
        Request request = new Request.Builder()
                .url("https://auth.roblox.com/v2/logout")
                .post(RequestBody.create(new byte[0], null))
                .header("Cookie", ".ROBLOSECURITY=%s".formatted(this.cookie))
                .build();

        return Mono.create(sink -> this.newCall(request).enqueue(new Callback() {
            @Override
            @ExcludeFromJacocoGeneratedReport
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                sink.error(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                // The token is in the "x-csrf-token" header.
                String token = response.headers().get("x-csrf-token");

                // If the token is null, the cookie was invalid.
                if (token == null)
                    sink.error(new InvalidCookieException());

                sink.success(token);
            }
        }));
    }

    /**
     * This method will set the Roblox cookie field.
     * @param cookie The Roblox cookie.
     */
    public void setCookie(String cookie) throws InvalidCookieException {
        // If the cookie doesn't begin with ".ROBLOSECURITY," then it's invalid.
        if (cookie != null && cookie.length() > 0 && !cookie.startsWith(COOKIE_SIGNATURE))
            throw new InvalidCookieException();

        this.cookie = cookie;
    }
}