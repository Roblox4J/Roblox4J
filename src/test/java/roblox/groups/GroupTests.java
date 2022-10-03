package roblox.groups;

import net.gestalt.roblox.client.Client;
import net.gestalt.roblox.groups.Group;
import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GroupTests {
    private final Client client = new Client();

    /**
     * This will test if the client is able to find a group.
     */
    @Test
    public void testGetGroup_WhenGroupIdValid() {
        // Pre-test.
        Group group = this.client.getGroup("14148840").block();

        // Test.
        assertNotNull(group);
    }

    /**
     * This will test if the client is able to throw an error when a group doesn't exist.
     */
    @Test
    public void testGetGroup_WhenGroupIdInvalid() {
        // Pre-test
        AtomicBoolean happened = new AtomicBoolean(false);

        this.client.getGroup("-1")
                .doOnError(e -> happened.set(true))
                .onErrorResume(e -> Mono.empty())
                .block();

        // Test.
        assertTrue(happened.get());
    }
}