package roblox.groups;

import net.gestalt.exceptions.AlreadyOwnedException;
import net.gestalt.exceptions.InvalidCookieException;
import net.gestalt.exceptions.InvalidIdException;
import net.gestalt.exceptions.ModeratedException;
import net.gestalt.roblox.client.Client;
import net.gestalt.roblox.groups.Group;
import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GroupTests {
    private final Client client = new Client();
    private final Group group;

    public GroupTests() throws InvalidCookieException {
        // Set Roblox cookie.
        this.client.setCookie(System.getProperty("robloSecurity") != null ? System.getProperty("robloSecurity") :
                System.getenv("robloSecurity"));

        // Pre-test.
        Group group = this.client.getGroup("15639111").block();

        // Test.
        assertNotNull(group);

        // Set it for future use.
        this.group = group;
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

    /**
     * This test will run once and only once. This will test if we are able to set a group name.
     */
    @Test
    public void testSetName_WhenDebounce() {
        // Test.
        // This should never error.
        this.group.setName("NAMENAMENAMECOOLNAME288")
                .onErrorResume(e -> Mono.empty())
                .block();
    }

    /**
     * This method will test if we are able to set the description.
     */
    @Test
    public void testSetDescription_WhenDescriptionValid() {
        // Test.
        this.group.setDescription("Hai").block();
    }

    /**
     * This will test if we are able to detect if the content was moderated or not.
     */
    @Test
    public void testSetDescription_WhenModerated() {
        // Pre-test.
        AtomicBoolean happened = new AtomicBoolean(false);

        this.group.setDescription("Fuck") // :(
                .doOnError(ModeratedException.class, e -> happened.set(true))
                .onErrorResume(ModeratedException.class, e -> Mono.empty())
                .block();

        // Test.
        assertTrue(happened.get());
    }

    /**
     * This will test if we are able to send a shout.
     */
    @Test
    public void testSendShout_WhenShoutValid() {
        // Test.
        this.group.sendShout("Hello...").block();
    }

    /**
     * This will test if an invalid id exception is thrown when an invalid account is provided.
     */
    @Test
    public void testAcceptAccount_WhenAccountInvalid() {
        // Pre-test.
        AtomicBoolean happened = new AtomicBoolean(false);

        this.group.acceptAccountRequest("-1")
                .doOnError(InvalidIdException.class, e -> happened.set(true))
                .onErrorResume(InvalidIdException.class, e -> Mono.empty())
                .block();

        // Test.
        assertTrue(happened.get());
    }

    /**
     * This will test if an error is thrown when we try to kick an invalid account.
     */
    @Test
    public void testKickAccount_WhenAccountInvalid() {
        // Pre-test.
        AtomicBoolean happened = new AtomicBoolean(false);

        this.group.kickAccount("-1")
                .doOnError(InvalidIdException.class, e -> happened.set(true))
                .onErrorResume(InvalidIdException.class, e -> Mono.empty())
                .block();

        // Test.
        assertTrue(happened.get());
    }

    /**
     * This will test if an error is thrown when trying to change an owner to an invalid id.
     */
    @Test
    public void testChangeOwner_WhenAccountInvalid() {
        // Pre-test.
        AtomicBoolean happened = new AtomicBoolean(false);

        this.group.changeOwner("-1")
                .doOnError(InvalidIdException.class, e -> happened.set(true))
                .onErrorResume(InvalidIdException.class, e -> Mono.empty())
                .block();

        // Test.
        assertTrue(happened.get());
    }

    /**
     * This will try to claim the group when it is already owned.
     */
    @Test
    public void testClaimOwnership_WhenGroupOwned() {
        // Pre-test.
        AtomicBoolean happened = new AtomicBoolean(false);

        this.group.claimOwnership()
                .doOnError(AlreadyOwnedException.class, e -> happened.set(true))
                .onErrorResume(AlreadyOwnedException.class, e -> Mono.empty())
                .block();

        // Test.
        assertTrue(happened.get());
    }
}