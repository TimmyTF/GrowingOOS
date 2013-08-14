package auctionsniper;

import java.util.EventListener;

/**
 * User: tflomin
 * Date: 29.07.13
 * Time: 12:55
 */
public interface AuctionEventListener extends EventListener {
    public enum PriceSource {
        FromSniper, FromOtherBidder
    }

    void auctionClosed();

    void currentPrice(Integer price, Integer increment, PriceSource priceSource);

    void auctionFailed();
}
