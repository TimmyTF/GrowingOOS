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
            /* 1. We call the application through its main() function to make sure we’ve assembled the pieces correctly.
            We’re following the convention that the entry point to the application is a Main class in the top-level package.
            WindowLicker can control Swing components if they’re in the same JVM, so we start the
            Sniper in a new thread. Ideally, the test would start the Sniper in a new process, but that would be much
            harder to test; we think this is a reasonable compromise. */
            @Override
            public void run() {
                try {
                    /* 2. To keep things simple at this stage, we’ll assume that we’re only bidding for
                    one item and pass the identifier to main(). */
                    Main.main(new String[] {XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId()});
                } catch (Exception e) {
                    /* 3. If main() throws an exception, we just print it out. Whatever test we’re
                    running will fail and we can look for the stack trace in the output. Later,
                    we’ll handle exceptions properly. */
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        /* 4. We turn down the timeout period for finding frames and components. The
        default values are longer than we need for a simple application like this one
        and will slow down the tests when they fail. We use one second, which is
        enough to smooth over minor runtime delays. */
        driver = new AuctionSniperDriver(1000);
        /* 5. We wait for the status to change to Joining so we know that the application
        has attempted to connect. This assertion says that somewhere in the user
        interface there’s a label that describes the Sniper’s state */
        driver.showsSniperStatus(Main.STATUS_JOINING);
    }

    public void showsSniperHasLostAuction() {
        /* 6. When the Sniper loses the auction, we expect it to show a Lost status. If this
        doesn’t happen, the driver will throw an exception. */
        driver.showsSniperStatus(Main.STATUS_LOST);
    }

    public void hasShownSniperIsBidding() {
        /* 6. When the Sniper loses the auction, we expect it to show a Lost status. If this
        doesn’t happen, the driver will throw an exception. */
        driver.showsSniperStatus(Main.STATUS_BIDDING);
    }

    public void stop() {
        /* 7. After the test, we tell the driver to dispose of the window to make sure it
        won’t be picked up in another test before being garbage-collected. */
        if (driver != null)
            driver.dispose();
    }
}
