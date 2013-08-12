package unit;

import auctionsniper.ui.MainWindow;
import auctionsniper.ui.SnipersTableModel;
import auctionsniper.ui.UserRequestListener;
import com.objogate.wl.swing.probe.ValueMatcherProbe;
import endtoend.AuctionSniperDriver;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

/**
 * User: tflomin
 * Date: 12.08.13
 * Time: 17:19
 */
public class MainWindowTest {
    private final SnipersTableModel tableModel = new SnipersTableModel();
    private final MainWindow mainWindow = new MainWindow(tableModel);
    private final AuctionSniperDriver driver = new AuctionSniperDriver(100);

    @Test
    public void
    makesUserRequestWhenJoinButtonClicked() {
        final ValueMatcherProbe<String> buttonProbe = new ValueMatcherProbe<String>(equalTo("an item-id"), "join request");
        mainWindow.addUserRequestListener(
            new UserRequestListener() {
                public void joinAuction(String itemId) {
                    buttonProbe.setReceivedValue(itemId);
                }
            });
        driver.startBiddingFor("an item-id");
        driver.check(buttonProbe);
    }
}
