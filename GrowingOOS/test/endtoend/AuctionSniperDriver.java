package endtoend;

import auctionsniper.Main;
import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

import static org.hamcrest.CoreMatchers.equalTo;

/**
 * User: tflomin
 * Date: 19.06.13
 * Time: 9:31
 */
public class AuctionSniperDriver extends JFrameDriver {


    public AuctionSniperDriver(int timeoutMillis) {
        super(new GesturePerformer(), JFrameDriver.topLevelFrame(named(Main.MAIN_WINDOW_NAME), showingOnScreen()), new AWTEventQueueProber(timeoutMillis, 100));
    }

    public void showsSniperStatus(String statusText) {
        new JLabelDriver(this,named(Main.SNIPER_STATUS_NAME)).hasText(equalTo(statusText));
    }
}
