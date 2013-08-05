package auctionsniper;

/**
 * User: tflomin
 * Date: 29.07.13
 * Time: 12:55
 */
public interface AuctionEventListener {
    public enum PriceSource {
        FromSniper, FromOtherBidder
    }

    public void auctionClosed();

    public void currentPrice(Integer price, Integer increment, PriceSource priceSource);
}
