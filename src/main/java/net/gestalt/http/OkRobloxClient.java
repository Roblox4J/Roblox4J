package net.gestalt.http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.io.IOException;

public class OkRobloxClient extends OkHttpClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(OkRobloxClient.class);
    private static final Gson GSON = new Gson();

    /**
     * Creates a Roblox client from the provided builder.
     * @param builder The HTTP builder.
     */
    public OkRobloxClient(@NotNull Builder builder) {
        super(builder);
    }

    /**
     * Creates a Roblox client.
     */
    public OkRobloxClient() {
        super();
    }

    /**
     * This method will execute a OkHttpClient call using the reactor core.
     * @param request The corresponding request.
     * @param <T> The class that the response is being cast to.
     * @return A reactor object.
     */
    public <T> Mono<T> execute(Request request, Class<T> t) {
        return Mono.create(sink -> super.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                sink.error(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // Make sure to catch the error if gson cannot cast.
                try {
                    T result = GSON.fromJson(response.body().string(), t);
                    sink.success(result);
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
     * This method will quietly close the provided response.
     * @param response The response.
     */
    private void closeQuietly(@NotNull Response response) {
        try {
            response.body();
            response.close();
        } catch (Exception e) {
            LOGGER.warn("Failed to silently close response.", e);
        }
    }
}