package roblox.accounts;

import net.gestalt.exceptions.InvalidCookieException;
import net.gestalt.roblox.accounts.Account;
import net.gestalt.roblox.client.Client;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountTests {
    private final Client client = new Client();
    private final long accountId = 3222418374L;
    private final Account account = this.client.getAccount(accountId).block();

    public AccountTests() throws InvalidCookieException {
        String cookie = System.getProperty("robloSecurity") != null ? System.getProperty("robloSecurity") :
                System.getenv("robloSecurity");
        this.client.setCookie(cookie);
    }

    /**
     * This will test if we're able to get all previous usernames.
     */
    @Test
    public void testGetPastUsernames() {
        // Test.
        assertNotNull(this.account);
        this.account.getUsernameHistory().blockLast(); // We are just making sure this doesn't error.
    }

    /**
     * This will test if the can message method works.
     */
    @Test
    public void testCanMessage() {
        // Test.
        assertNotNull(this.account);
        assertEquals(Boolean.FALSE, this.account.canMessage().block()); // We can't send messages to ourselves.
    }

    /**
     * This method will test if we are able to get an account's friends.
     */
    @Test
    public void testCanGetFriends() {
        // Test.
        assertNotNull(this.account);
        this.account.getFriends().block();
    }

    @Test
    public void testSendMessage() {
        // Test.
        assertNotNull(this.account);
        this.account.sendMessage("Hi", "Hello").block();
    }
}