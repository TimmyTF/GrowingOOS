package auctionsniper;

import auctionsniper.ui.Announcer;
import auctionsniper.ui.UserRequestListener;

/**
 * User: tflomin
 * Date: 29.07.13
 * Time: 18:35
 */
public class AuctionSniper implements AuctionEventListener {
    private final Announcer<SniperListener> listeners = Announcer.to(SniperListener.class);
    private final Auction auction;
    private SniperSnapshot snapshot;
    private final UserRequestListener.Item item;

    public AuctionSniper(UserRequestListener.Item item, Auction auction) {
        this.item = item;
        this.auction = auction;
        this.snapshot = SniperSnapshot.joining(item.identifier);
    }

    public void auctionClosed() {
        snapshot = snapshot.closed();
        notifyChange();
    }

    public void auctionFailed() {
        snapshot = snapshot.failed();
        notifyChange();
    }

    public void currentPrice(Integer price, Integer increment, PriceSource priceSource) {
        switch(priceSource) {
            case FromSniper:
                snapshot = snapshot.winning(price);
                break;
            case FromOtherBidder:
                int bid = price + increment;
                if (item.allowsBid(bid)) {
                    auction.bid(bid);
                    snapshot = snapshot.bidding(price, bid);
                } else {
                    snapshot = snapshot.losing("" + price);
                }
                break;
        }
        notifyChange();
    }

    public void addSniperListener(SniperListener listener) {
        listeners.addListener(listener);
    }

    private void notifyChange() {
        listeners.announce().sniperStateChanged(snapshot);
    }

    public SniperSnapshot getSnapshot() {
        return snapshot;
    }
}
