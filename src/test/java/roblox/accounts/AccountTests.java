package roblox.accounts;

import net.gestalt.roblox.accounts.Account;
import net.gestalt.roblox.client.Client;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class AccountTests {
    private final Client client = new Client();
    private final long accountId = 1116067525L;
    private final Account account = this.client.getAccount(accountId).block();

    /**
     * This will test if we're able to get all previous usernames.
     */
    @Test
    public void testGetPastUsernames() {
        assertNotNull(this.account);
        this.account.getUsernameHistory().blockLast();
    }
}