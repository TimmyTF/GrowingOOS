package auctionsniper;

/**
 * User: tflomin
 * Date: 29.07.13
 * Time: 18:35
 */
public class AuctionSniper implements AuctionEventListener {
    private final SniperListener sniperListener;
    private final Auction auction;

    public AuctionSniper(Auction auction, SniperListener sniperListener) {
        this.sniperListener = sniperListener;
        this.auction = auction;
    }

    public void auctionClosed() {
        sniperListener.sniperLost();
    }

    public void currentPrice(Integer price, Integer increment, PriceSource priceSource) {
        auction.bid(price + increment);
        sniperListener.sniperBidding();
        // TODO: use priceSource somehow
    }

    public void sniperLost() {
        sniperListener.sniperLost();
    }
}
