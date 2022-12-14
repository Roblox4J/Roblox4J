package roblox.client;

import roblox4j.exceptions.InvalidAccountNameException;
import roblox4j.exceptions.InvalidCookieException;
import roblox4j.roblox.accounts.Account;
import roblox4j.roblox.client.Client;
import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class ClientTests {
    private static final long ROBLOX_ID = 1;
    final Client client = new Client();

    /**
     * This will test if the HTTP Roblox client is able to detect if an error occurs in the API.
     */
    @Test
    public void testGetAccountWithId_WhenIdInvalid() {
        AtomicBoolean happened = new AtomicBoolean(false);

        // Pre-test.
        this.client.getAccount(-1L)
                .doOnError(r -> happened.set(true))
                .onErrorResume(e -> Mono.empty())
                .block();

        // Test.
        assertTrue(happened.get());
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
        assertEquals(account.id(), ROBLOX_ID);
    }

    /**
     * This will test if the client is able to find a username by its username.
     */
    @Test
    public void testGetAccountByUsername_WhenUsernameValid() {
        // Pre-test.
        Account account = this.client.getAccountFromUsername("Roblox").block();

        // Test.
        assertNotNull(account);
        assertEquals(account.id(), 1);
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
                .onErrorResume(InvalidAccountNameException.class, e -> Mono.empty())
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
        String token = System.getProperty("robloSecurity") != null ? System.getProperty("robloSecurity") :
                System.getenv("robloSecurity");
        this.client.setCookie(token);
        Account account = this.client.getAuthenticatedAccount().block();

        // Test.
        assertNotNull(account);
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
                .doOnError(e -> happened.set(true))
                .onErrorResume(e -> Mono.empty())
                .block();

        assertTrue(happened.get());
    }

    /**
     * This method will test if we are able to fetch a popular game.
     */
    @Test
    public void testGetGame_WhenIdValid() {
        this.client.getGame("2753915549").block();
    }

    /**
     * This will test if we are able to detect an invalid id.
     */
    @Test
    public void testGetGame_WhenIdInvalid() {
        AtomicBoolean happened = new AtomicBoolean(false);

        this.client.getGame("-1")
                .doOnError(e -> happened.set(true))
                .onErrorResume(e -> Mono.empty())
                .block();

        assertTrue(happened.get());
    }

    /**
     * This will test if we are able to retrieve a model.
     */
    @Test
    public void testGetModel_WhenIdValid() {
        this.client.getModel("1351862808").block();
    }

    /**
     * This will test if the invalid id error is thrown when we try to fetch an invalid model.
     */
    @Test
    public void testGetModel_WhenIdInvalid() {
        AtomicBoolean happened = new AtomicBoolean(false);

        this.client.getModel("-1")
                .doOnError(e -> happened.set(true))
                .onErrorResume(e -> Mono.empty())
                .block();

        assertTrue(happened.get());
    }
}