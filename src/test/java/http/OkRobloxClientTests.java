package http;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import net.gestalt.http.OkRobloxClient;
import okhttp3.Request;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Getter
class EchoResponse {
    @Expose
    private String url;
}

@Getter
class NestedHeaders {
    @Expose
    private String host;
}

@Getter
class NestedResponse {
    @Expose
    private NestedHeaders headers;
}

public class OkRobloxClientTests {
    private final String ECHO_URL = "https://postman-echo.com/get";
    private final OkRobloxClient okRobloxClient = new OkRobloxClient();

    /**
     * This will test if the client is able to send a simple ping request. It'll also test basic casting.
     */
    @Test
    public void testResponse() {
        // Post-test.
        Request request = new Request.Builder()
                .url(ECHO_URL)
                .build();
        EchoResponse response = this.okRobloxClient.execute(request, EchoResponse.class).block();

        // Tests.
        assertNotNull(response);
        assertEquals(response.getUrl(), "https://postman-echo.com/get");
    }

    /**
     * This will test if the parser recognizes nested information.
     */
    @Test
    public void testNestedResponse() {
        // Post-test.
        Request request = new Request.Builder()
                .url(ECHO_URL)
                .build();
        NestedResponse response = this.okRobloxClient.execute(request, NestedResponse.class).block();

        // Tests.
        assertNotNull(response);
        assertEquals(response.getHeaders().getHost(), "postman-echo.com");
    }
}