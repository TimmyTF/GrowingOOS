package endtoend;

import auctionsniper.Main;

/**
 * User: tflomin
 * Date: 18.06.13
 * Time: 16:09
 */
public class ApplicationRunner {
    public static final String XMPP_HOSTNAME = "localhost";

    private String itemId;

    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_XMPP_ID = "sniper@localhost/Auction";
    public static final String SNIPER_PASSWORD = "1";

    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer auction) {
        itemId = auction.getItemId();
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(new String[] {XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId()});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        driver = new AuctionSniperDriver(1000);
        driver.showsSniperStatus(itemId, 0, 0, Main.STATUS_JOINING);
    }

    public void showsSniperHasLostAuction(int lastPrice, int lastBid) {
        driver.showsSniperStatus(itemId, lastPrice, lastBid, Main.STATUS_LOST);
    }

    public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
        driver.showsSniperStatus(itemId, lastPrice, lastBid, Main.STATUS_BIDDING);
    }

    public void hasShownSniperIsWinning(int winningBid) {
        driver.showsSniperStatus(itemId, winningBid, winningBid, Main.STATUS_WINNING);
    }

    public void showsSniperHasWonAuction(int lastPrice) {
        driver.showsSniperStatus(itemId, lastPrice, lastPrice, Main.STATUS_WON);
    }

    public void stop() {
        if (driver != null)
            driver.dispose();
    }
}
