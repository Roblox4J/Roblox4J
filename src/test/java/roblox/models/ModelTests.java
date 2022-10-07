package roblox.models;

import roblox4j.exceptions.InvalidCookieException;
import roblox4j.roblox.accounts.Account;
import roblox4j.roblox.client.Client;
import roblox4j.roblox.models.Model;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ModelTests {
    private final Model model;
    private final Account us;

    public ModelTests() throws InvalidCookieException {
        // Set Roblox cookie.
        Client client = new Client();
        client.setCookie(System.getProperty("robloSecurity") != null ? System.getProperty("robloSecurity") :
                System.getenv("robloSecurity"));

        // Pre-test.
        Model model = client.getModel("1351862808").block();
        Account us = client.getAuthenticatedAccount().block();

        // Test.
        assertNotNull(model);
        assertNotNull(us);

        // Set it for future use.
        this.model = model;
        this.us = us;
    }

    @Test
    public void testBuyAndIsOwned() {
        // Pre-test.
        this.model.buy().block();
        boolean isOwned = Boolean.TRUE.equals(this.model.isOwned(this.us).block());

        // Test.
        assertTrue(isOwned);
    }

    @Test
    public void testFavorite() {
        // Test.
        this.model.toggleFavorite().block();
    }

    @Test
    public void testVote() {
        // Test.
        this.model.vote(true).block();
    }

    @Test

    public void testDelete() {
        // Test.
        this.model.delete().block();
    }
}