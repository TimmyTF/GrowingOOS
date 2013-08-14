package auctionsniper;

import auctionsniper.ui.SnipersTableModel;
import auctionsniper.ui.UserRequestListener;

import java.util.ArrayList;

/**
 * User: tflomin
 * Date: 13.08.13
 * Time: 17:07
 */
public class SniperLauncher implements UserRequestListener {
    private final AuctionHouse auctionHouse;
    private final SniperCollector collector;

    public SniperLauncher(AuctionHouse auctionHouse, SniperCollector collector) {
        this.auctionHouse = auctionHouse;
        this.collector = collector;
    }

    public void joinAuction(Item item) {
        Auction auction = auctionHouse.auctionFor(item);
        AuctionSniper sniper = new AuctionSniper(item, auction);
        auction.addAuctionEventListener(sniper);
        collector.addSniper(sniper);
        auction.join();
    }
}
