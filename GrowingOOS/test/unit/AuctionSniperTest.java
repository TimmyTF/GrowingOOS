package unit;

import auctionsniper.*;
import auctionsniper.ui.SnipersTableModel;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.equalTo;

import static auctionsniper.AuctionEventListener.PriceSource.*;

/**
 * User: tflomin
 * Date: 29.07.13
 * Time: 18:41
 */
@RunWith(JMock.class)
public class AuctionSniperTest {
    public static final String ITEM_ID = "item-54321";
    private final Mockery context = new Mockery();
    private final Auction auction = context.mock(Auction.class);
    private final SniperListener sniperListener = context.mock(SniperListener.class);
    private final AuctionSniper sniper = new AuctionSniper(ITEM_ID, auction, sniperListener);
    private final States sniperState = context.states("sniper");

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;
        final int bid = price + increment;
        context.checking(new Expectations() {{
            one(auction).bid(bid);
            atLeast(1).of(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, price, bid, SniperState.BIDDING));
        }});

        sniper.currentPrice(price, increment, FromOtherBidder);
    }

    @Test
    public void reportsIsWinningWhenCurrentPriceComesFromSniper() {
        allowingSniperBidding();
        ignoringAuction();
        context.checking(new Expectations() {{
            atLeast(1).of(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, 135, 135, SniperState.WINNING));
            when(sniperState.is("bidding"));
        }});

        sniper.currentPrice(123, 12, FromOtherBidder);
        sniper.currentPrice(135, 45, FromSniper);
    }

    @Test
    public void reportsLostIfAuctionClosesWhenBidding() {
        allowingSniperBidding();
        ignoringAuction();

        context.checking(new Expectations() {{
            atLeast(1).of(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, 123, 168, SniperState.LOST));
            when(sniperState.is("bidding"));
        }});

        sniper.currentPrice(123, 45, FromOtherBidder);
        sniper.auctionClosed();
    }

    @Test
    public void reportsWonIfAuctionClosesWhenWinning() {
        allowingSniperBidding();
        allowingSniperWinning();
        ignoringAuction();

        context.checking(new Expectations() {{
          atLeast(1).of(sniperListener).sniperStateChanged( new SniperSnapshot(ITEM_ID, 135, 135, SniperState.WON));
            when(sniperState.is("winning"));
        }});

        sniper.currentPrice(123, 12, FromOtherBidder);
        sniper.currentPrice(135, 45, FromSniper);
        sniper.auctionClosed();
    }

    private Matcher<SniperSnapshot> aSniperThatIs(final SniperState state) {
        return new FeatureMatcher<SniperSnapshot, SniperState>(equalTo(state), "sniper that is ", "was") {
            @Override
            protected SniperState featureValueOf(SniperSnapshot actual) {
                return actual.state;
            }
        };
    }

    private void allowingSniperBidding() {
        allowSniperStateChange(SniperState.BIDDING, "bidding");
    }

    private void allowingSniperWinning() {
        allowSniperStateChange(SniperState.WINNING, "winning");
    }

    private void allowSniperStateChange(final SniperState newState, final String oldState) {
        context.checking(new Expectations() {{
            allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(newState))); then(sniperState.is(oldState));
        }});
    }

    private void ignoringAuction() {
        context.checking(new Expectations() {{
            ignoring(auction);
        }});
    }
}
