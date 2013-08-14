package endtoend;

import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.logging.LogManager;

import auctionsniper.XMPPAuctionHouse;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matcher;

/**
 * User: tflomin
 * Date: 13.08.13
 * Time: 18:53
 */
public class AuctionLogDriver {
    private final File logFile = new File(XMPPAuctionHouse.LOG_FILE_NAME);

    public void hasEntry(Matcher<String> matcher) throws IOException  {
        assertThat(FileUtils.readFileToString(logFile), matcher);
    }

    public void clearLog() {
        logFile.delete();
        LogManager.getLogManager().reset();
    }
}
