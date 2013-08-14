package auctionsniper.ui;

import java.util.logging.Logger;

/**
 * User: tflomin
 * Date: 13.08.13
 * Time: 17:56
 */
public class LoggingXMPPFailureReporter implements XMPPFailureReporter {
    private static final String MESSAGE_FORMAT = "<%s> Could not translate message \"%s\" because \"%s\"";
    private final Logger logger;

    public LoggingXMPPFailureReporter(Logger logger) {
        this.logger = logger;
    }

    public void cannotTranslateMessage(String auctionId, String failedMessage, Exception exception) {
        logger.severe(String.format(MESSAGE_FORMAT, auctionId, failedMessage, exception.toString()));
    }
}