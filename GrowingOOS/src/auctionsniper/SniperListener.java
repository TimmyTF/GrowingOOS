package auctionsniper;

import java.util.EventListener;

/**
 * User: tflomin
 * Date: 29.07.13
 * Time: 18:39
 */
public interface SniperListener extends EventListener {
    void sniperLost();

    void sniperBidding(SniperSnapshot status);

    void sniperWinning();

    void sniperWon();
}
