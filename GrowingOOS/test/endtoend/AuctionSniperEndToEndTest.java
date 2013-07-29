package endtoend;

import org.junit.After;
import org.junit.Test;

/**
 * User: tflomin
 * Date: 17.06.13
 * Time: 18:39
 */
public class AuctionSniperEndToEndTest {
    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
    private final ApplicationRunner application = new ApplicationRunner();

    @Test
    public void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
        auction.startSellingItem();
        application.startBiddingIn(auction); // why not to call this method 'join' or smth?
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);
        auction.announceClosed();
        application.showsSniperHasLostAuction();
    }

    @Test
    public void sniperMakesAHigherBidButLoses() throws Exception {
        auction.startSellingItem();
        application.startBiddingIn(auction);
        // 1. We have to wait for the stub auction to receive the Join request before continuing with the test.
        // We use this assertion to synchronize the Sniper with the auction.
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);
        // 2. This method tells the stub auction to send a message back to the Sniper with
        // the news that at the moment the price of the item is 1000, the increment for
        // the next bid is 98, and the winning bidder is "other bidder"
        auction.reportPrice(1000, 98, "other bidder");
        // 3. This method asks the ApplicationRunner to check that the Sniper shows that
        // it’s now bidding after it’s received the price update message from the auction.
        application.hasShownSniperIsBidding();
        // 4. This method asks the stub auction to check that it has received a bid from
        // the Sniper that is equal to the last price plus the minimum increment. We
        // have to do a fraction more work because the XMPP layer constructs a longer
        // name from the basic identifier, so we define a constant SNIPER_XMPP_ID which
        // in practice is sniper@localhost/Auction.
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
        // 5. We reuse the closing logic from the first test, as the Sniper still loses the
        // auction.
        auction.announceClosed();
        application.showsSniperHasLostAuction();
    }

    // Additional cleanup
    @After
    public void stopAuction() {
        auction.stop();
    }

    @After
    public void stopApplication() {
        application.stop();
    }
}
