package unit;

import auctionsniper.AuctionEventListener;
import auctionsniper.AuctionMessageTranslator;
import auctionsniper.ui.XMPPFailureReporter;
import org.jivesoftware.smack.Chat;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.jivesoftware.smack.packet.Message;
import org.junit.runner.RunWith;

import static endtoend.ApplicationRunner.SNIPER_ID;

/**
 * User: tflomin
 * Date: 29.07.13
 * Time: 12:58
 */
@RunWith(JMock.class)
public class AuctionMessageTranslatorTest {
    private final Mockery context = new Mockery();
    public static final Chat UNUSED_CHAT = null;
    private final AuctionEventListener listener = context.mock(AuctionEventListener.class);
    private final XMPPFailureReporter failureReporter = context.mock(XMPPFailureReporter.class);
    private final AuctionMessageTranslator translator = new AuctionMessageTranslator(SNIPER_ID, listener, failureReporter);

    @Test
    public void notifiesAuctionClosedWhenCloseMessageReceived() {
        context.checking(new Expectations() {{
            oneOf(listener).auctionClosed();
        }});

        Message message = new Message();
        message.setBody("SOLVersion: 1.1; Event: CLOSE;");

        translator.processMessage(UNUSED_CHAT, message);
    }

    @Test
    public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder() {
        context.checking(new Expectations() {{
            exactly(1).of(listener).currentPrice(192, 7, AuctionEventListener.PriceSource.FromOtherBidder);
        }});

        Message message = new Message();
        message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;");
        translator.processMessage(UNUSED_CHAT, message);
    }

    @Test
    public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromSniper() {
        context.checking(new Expectations() {{
            exactly(1).of(listener).currentPrice(234, 5, AuctionEventListener.PriceSource.FromSniper);
        }});
        Message message = new Message();
        message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 234; Increment: 5; Bidder: " + SNIPER_ID + ";");
        translator.processMessage(UNUSED_CHAT, message);
    }
}
