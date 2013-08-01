package endtoend;

import auctionsniper.Main;

/**
 * User: tflomin
 * Date: 18.06.13
 * Time: 16:09
 */
public class ApplicationRunner {
    public static final String XMPP_HOSTNAME = "localhost";


    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_XMPP_ID = "sniper@localhost/Auction";
    public static final String SNIPER_PASSWORD = "1";

    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer auction) {
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
        driver.showsSniperStatus(Main.STATUS_JOINING);
    }

    public void showsSniperHasLostAuction() {
        driver.showsSniperStatus(Main.STATUS_LOST);
    }

    public void hasShownSniperIsBidding() {
        driver.showsSniperStatus(Main.STATUS_BIDDING);
    }

    public void stop() {
        if (driver != null)
            driver.dispose();
    }
}
