package roblox.client;

import net.gestalt.exceptions.InvalidCookieException;
import net.gestalt.exceptions.InvalidRequestException;
import net.gestalt.roblox.accounts.Account;
import net.gestalt.roblox.client.Client;
import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class ClientTests {
    private static final long AUTHENTICATED_ID = 3925926743L; // TODO: CHANGE ME!
    private static final long ROBLOX_ID = 1;
    private static final int INVALID_USER_ID = 3;
    Client client = new Client();

    /**
     * This will test if the HTTP Roblox client is able to detect if an error occurs in the API.
     */
    @Test
    public void testGetAccountWithId_WhenIdInvalid() {
        // Test.
        this.client.getAccount(-1L)
                .doOnError(InvalidRequestException.class,
                        invalidRequestException -> assertEquals(invalidRequestException.getCode(), INVALID_USER_ID))
                .onErrorResume(e -> Mono.empty())
                .block();
    }

    /**
     * This will test if the client is able to find an account.
     */
    @Test
    public void testGetAccountWithId_WhenIdValid() {
        // Pre-test.
        Account account = this.client.getAccount(ROBLOX_ID).block();

        // Test.
        assertNotNull(account);
        assertEquals(account.getId(), ROBLOX_ID);
    }

    /**
     * This will test if the client is able to find a username by it's username.
     */
    @Test
    public void testGetAccountByUsername_WhenUsernameValid() {
        // Pre-test.
        Account account = this.client.getAccountFromUsername("Roblox").block();

        // Test.
        assertNotNull(account);
        assertEquals(account.getId(), 1);
    }

    /**
     * This will test if the method errors if an invalid name is provided.
     */
    @Test
    public void testGetAccountByUsername_WhenUsernameInvalid() {
        AtomicBoolean happened = new AtomicBoolean(false);

        // Test.
        this.client.getAccountFromUsername("INVALIDINVALIDINVALIDINVALIDINVALIDINVALID")
                .doOnError(r -> happened.set(true))
                .onErrorResume(e -> Mono.empty())
                .block();

        assertTrue(happened.get());
    }

    /**
     * This will test if the client is able to retrieve the authenticated account.
     * @throws InvalidCookieException When the cookie is invalid.
     */
    @Test
    public void testGetAuthenticatedAccount_WhenAuthenticatedValid() throws InvalidCookieException {
        // Pre-test.
        // We have to add our cookie in order to authenticate.
        System.out.println(System.getenv("robloSecurity"));
        this.client.setCookie(System.getenv("robloSecurity"));
        Account account = this.client.getAuthenticatedAccount().block();

        // Test.
        assertNotNull(account);
        assertEquals(account.getId(), AUTHENTICATED_ID);
    }

    /**
     * This will test if the client is able to throw an error when the cookie isn't valid.
     * @throws InvalidCookieException When the cookie is invalid. It should throw this error.
     */
    @Test
    public void testGetAuthenticatedAccount_WhenAuthenticatedInvalid() throws InvalidCookieException {
        AtomicBoolean happened = new AtomicBoolean(false);

        this.client.setCookie(null);
        this.client.getAuthenticatedAccount()
                .doOnError(r -> happened.set(true))
                .onErrorResume(r -> Mono.empty())
                .block();

        assertTrue(happened.get());
    }
}