package auctionsniper;

import com.objogate.exception.Defect;

/**
 * User: tflomin
 * Date: 07.08.13
 * Time: 16:20
 */
public enum SniperState {
    JOINING {
        @Override public SniperState whenAuctionClosed() { return LOST; }
    },

    BIDDING {
        @Override public SniperState whenAuctionClosed() { return LOST; }
    },

    WINNING {
        @Override public SniperState whenAuctionClosed() { return WON; }
    },

    LOST,

    WON;

    public SniperState whenAuctionClosed() {
        throw new Defect("Auction is already closed");
    }
}
