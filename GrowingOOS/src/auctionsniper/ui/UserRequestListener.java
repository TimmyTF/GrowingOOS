package auctionsniper.ui;

import java.util.EventListener;

/**
 * User: tflomin
 * Date: 12.08.13
 * Time: 17:13
 */
public interface UserRequestListener extends EventListener {
    void joinAuction(String itemId);
}
