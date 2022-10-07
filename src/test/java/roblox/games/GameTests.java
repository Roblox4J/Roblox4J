package roblox.games;

import roblox4j.exceptions.InvalidCookieException;
import roblox4j.roblox.client.Client;
import roblox4j.roblox.games.Game;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class GameTests {
    private final Game game;

    public GameTests() throws InvalidCookieException {
        // Set Roblox cookie.
        Client client = new Client();
        client.setCookie(System.getProperty("robloSecurity") != null ? System.getProperty("robloSecurity") :
                System.getenv("robloSecurity"));

        // Pre-test.
        Game game = client.getGame("9458832163").block();

        // Test.
        assertNotNull(game);

        // Set it for future use.
        this.game = game;
    }

    @Test
    public void testFavorite() {
        this.game.favorite(true).block();
    }

    @Test
    public void testVote() {
        this.game.vote(true).block();
    }
}