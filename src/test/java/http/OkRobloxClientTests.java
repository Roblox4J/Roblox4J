package http;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import roblox4j.http.OkRobloxClient;
import okhttp3.Request;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Getter
class EchoResponse {
    @Expose
    private String url;
}

public class OkRobloxClientTests {
    private final OkRobloxClient okRobloxClient = new OkRobloxClient();

    /**
     * This will test if the client is able to send a simple ping request. It'll also test basic casting.
     */
    @Test
    public void testExecute_WhenResponseValid() {
        // Pre-test.
        String ECHO_URL = "https://postman-echo.com/get";
        Request request = new Request.Builder()
                .url(ECHO_URL)
                .build();
        EchoResponse response = this.okRobloxClient.execute(request, EchoResponse.class).block();

        // Tests.
        assertNotNull(response);
        assertEquals(response.getUrl(), "https://postman-echo.com/get");
    }
}