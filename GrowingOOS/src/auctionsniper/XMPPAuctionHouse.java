package auctionsniper;

import auctionsniper.ui.LoggingXMPPFailureReporter;
import auctionsniper.ui.UserRequestListener;
import auctionsniper.xmpp.XMPPAuction;
import auctionsniper.xmpp.XMPPAuctionException;
import org.apache.commons.io.FilenameUtils;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * User: tflomin
 * Date: 13.08.13
 * Time: 16:04
 */
public class XMPPAuctionHouse implements AuctionHouse {
    private static final String LOGGER_NAME = "auction-sniper";
    public static final String LOG_FILE_NAME = "auction-sniper.log";

    private final XMPPConnection connection;
    private final LoggingXMPPFailureReporter failureReporter;

    public XMPPAuctionHouse(XMPPConnection connection) throws XMPPAuctionException {
        this.connection = connection;
        this.failureReporter = new LoggingXMPPFailureReporter(makeLogger());
    }

    public Auction auctionFor(UserRequestListener.Item item) {
        return new XMPPAuction(connection, auctionId(item.identifier, connection), failureReporter);
    }

    private static String auctionId(String itemId, XMPPConnection connection) {
        return String.format(Main.AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }

    public static XMPPAuctionHouse connect(String hostname, String username, String password) throws XMPPAuctionException {
        XMPPConnection connection = new XMPPConnection(hostname);
        try {
            connection.connect();
            connection.login(username, password, Main.AUCTION_RESOURCE);
            return new XMPPAuctionHouse(connection);
        } catch (XMPPException xmppe) {
            throw new XMPPAuctionException("Could not connect to auction: " + connection, xmppe);
        }
    }

    private Logger makeLogger() throws XMPPAuctionException {
        Logger logger = Logger.getLogger(LOGGER_NAME);
        logger.setUseParentHandlers(false);
        logger.addHandler(simpleFileHandler());
        return logger;
    }

    private FileHandler simpleFileHandler() throws XMPPAuctionException {
        try {
            FileHandler handler = new FileHandler(LOG_FILE_NAME);
            handler.setFormatter(new SimpleFormatter());
            return handler;
        } catch (Exception e) {
            throw new XMPPAuctionException("Could not create logger FileHandler " + FilenameUtils.getFullPath(LOG_FILE_NAME), e);
        }
    }

    public void disconnect() {
        connection.disconnect();
    }

    /*TODO: get rid of this getter*/
    public XMPPConnection getConnection() {
        return connection;
    }
}
