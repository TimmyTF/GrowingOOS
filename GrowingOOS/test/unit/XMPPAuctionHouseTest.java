package unit;

import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;
import auctionsniper.Main;
import auctionsniper.XMPPAuctionHouse;
import auctionsniper.ui.UserRequestListener;
import auctionsniper.xmpp.XMPPAuction;
import auctionsniper.xmpp.XMPPAuctionException;
import endtoend.ApplicationRunner;
import endtoend.FakeAuctionServer;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * User: tflomin
 * Date: 13.08.13
 * Time: 12:21
 */
public class XMPPAuctionHouseTest {
    private XMPPAuctionHouse auctionHouse;
    private final FakeAuctionServer auctionServer = new FakeAuctionServer("item-54321");

    @Before
    public void openConnection() throws XMPPAuctionException {
        auctionHouse = XMPPAuctionHouse.connect(FakeAuctionServer.XMPP_HOSTNAME, ApplicationRunner.SNIPER_ID, ApplicationRunner.SNIPER_PASSWORD);

    }

    @After
    public void closeConnection() {
        if (auctionHouse != null) {
            auctionHouse.disconnect();
        }
    }

    @Before
    public void startAuction() throws XMPPException {
        auctionServer.startSellingItem();
    }

    @After
    public void stopAuction() {
        auctionServer.stop();
    }

    @Test public void
    receivesEventsFromAuctionServerAfterJoining() throws Exception {
        CountDownLatch auctionWasClosed = new CountDownLatch(1);

        Auction auction = auctionHouse.auctionFor(new UserRequestListener.Item(auctionServer.getItemId(), 567));
        auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));
        auction.join();
        auctionServer.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        auctionServer.announceClosed();

        assertTrue("should have been closed", auctionWasClosed.await(4, TimeUnit.SECONDS));
    }

    private AuctionEventListener auctionClosedListener(final CountDownLatch auctionWasClosed) {
        return new AuctionEventListener() {
            public void auctionClosed() {
                auctionWasClosed.countDown();
            }

            public void currentPrice(Integer price, Integer increment, AuctionEventListener.PriceSource priceSource) {
                // not implemented
            }

            public void auctionFailed() { }
        };
    }
}
