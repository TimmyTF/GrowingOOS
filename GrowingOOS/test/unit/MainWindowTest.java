package unit;

import auctionsniper.SniperPortfolio;
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
    private final MainWindow mainWindow = new MainWindow(new SniperPortfolio());
    private final AuctionSniperDriver driver = new AuctionSniperDriver(100);

    @Test public void
    makesUserRequestWhenJoinButtonClicked() {
        final ValueMatcherProbe<UserRequestListener.Item> itemProbe =
                new ValueMatcherProbe<UserRequestListener.Item>(equalTo(new UserRequestListener.Item("an item-id", 789)),
                        "item request");
        mainWindow.addUserRequestListener(
                new UserRequestListener() {
                    public void joinAuction(Item item) {
                        itemProbe.setReceivedValue(item);
                    }
                });

        driver.startBiddingWithStopPrice("an item-id", 789);
        driver.check(itemProbe);
    }
}
