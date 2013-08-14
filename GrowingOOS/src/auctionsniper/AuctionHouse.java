package auctionsniper;

import auctionsniper.ui.UserRequestListener;

/**
 * User: tflomin
 * Date: 13.08.13
 * Time: 16:02
 */
public interface AuctionHouse {
    Auction auctionFor(UserRequestListener.Item item);
}
