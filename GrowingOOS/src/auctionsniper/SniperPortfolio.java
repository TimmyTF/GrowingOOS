package auctionsniper;

import auctionsniper.ui.Announcer;
import auctionsniper.ui.SnipersTableModel;

import java.util.ArrayList;
import java.util.EventListener;

/**
 * User: tflomin
 * Date: 13.08.13
 * Time: 17:47
 */
public class SniperPortfolio implements SniperCollector {
    public interface PortfolioListener extends EventListener {
        void sniperAdded(AuctionSniper sniper);
    }

    private final Announcer<PortfolioListener> announcer = Announcer.to(PortfolioListener.class);
    private final ArrayList<AuctionSniper> snipers = new ArrayList<AuctionSniper>();

    public void addSniper(AuctionSniper sniper) {
        snipers.add(sniper);
        announcer.announce().sniperAdded(sniper);
    }

    public void addPortfolioListener(PortfolioListener listener) {
        announcer.addListener(listener);
    }
}

