package endtoend;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * User: tflomin
 * Date: 19.06.13
 * Time: 11:17
 */
public class FakeAuctionServer {
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String XMPP_HOSTNAME = "localhost"; // TODO: set this name to the XMPP server
    private static final String AUCTION_PASSWORD = "auction"; // TODO: check what's the real password

    private final String itemId;
    private final XMPPConnection connection;
    private Chat currentChat;
    private final SingleMessageListener messageListener = new SingleMessageListener();

    public FakeAuctionServer(String itemId) {
        this.itemId = itemId;
        this.connection = new XMPPConnection(XMPP_HOSTNAME);
    }

    public void startSellingItem() throws XMPPException {
        connection.connect();
        connection.login(String.format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, AUCTION_RESOURCE);
        connection.getChatManager().addChatListener(
                new ChatManagerListener() {
                    @Override
                    public void chatCreated(Chat chat, boolean createdLocally) {
                        currentChat = chat;
                        chat.addMessageListener(messageListener);
                    }
                }
        );
    }

    public String getItemId() {
        return itemId;
    }

    public void hasReceivedJoinRequestFromSniper() throws InterruptedException {
        /* 1. The test needs to know when a Join message has arrived. We just check
        whether any message has arrived, since the Sniper will only be sending Join
        messages to start with; we’ll fill in more detail as we grow the application.
        This implementation will fail if no message is received within 5 seconds. */
        messageListener.recievesAMessage();
    }

    public void announceClosed() throws XMPPException {
        /* 2. The test needs to be able to simulate the auction announcing when it closes,
        which is why we held onto the current Chat when it opened. As with the
        Join request, the fake auction just sends an empty message, since this is
        the only event we support so far. */
        currentChat.sendMessage(new Message());
    }

    public void stop() {
        /* 3. stop()closes the connection. */
        connection.disconnect();
    }

    public static class SingleMessageListener implements MessageListener {
        private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(1);

        public void processMessage(Chat chat, Message message) {
            messages.add(message);
        }

        public void recievesAMessage() throws InterruptedException {
            /* 4. The clause is(notNullValue()) uses the Hamcrest matcher syntax. We describe Matchers in
            "Methods" (page 339); for now, it’s enough to know that this checks that the Listener has received
            a message within the timeout period. */
            assertThat("Message", messages.poll(5, TimeUnit.SECONDS), is(notNullValue()));
        }
    }
}


