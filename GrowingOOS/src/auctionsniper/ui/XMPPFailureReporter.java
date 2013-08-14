package auctionsniper.ui;

/**
 * User: tflomin
 * Date: 13.08.13
 * Time: 17:57
 */
public interface XMPPFailureReporter {
    void cannotTranslateMessage(String auctionId, String failedMessage, Exception exception);
}
