package misc;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertFalse;

/**
 * User: tflomin
 * Date: 14.08.13
 * Time: 16:24
 */
public class RequestsReceiverTest {
    private final Request FIRST_REQUEST = new Request();
    private final Request SECOND_REQUEST = new Request();
    private final RequestsReceiver receiver = new RequestsReceiver();
    private final Clock stubClock = new Clock();
    private final Date TODAY = new Date();
    private final Date TOMORROW = new Date();

    @Test
    public void rejectsRequestsNotWithinTheSameDay() {
        // first day
        receiver.acceptRequest(FIRST_REQUEST);
        // the next day
        assertFalse("too late now", receiver.acceptRequest(SECOND_REQUEST));
    }

    @Test
    public void rejectsRequestsNotWithinTheSameDaySecondVersion() {
        RequestsReceiver receiver = new RequestsReceiver(stubClock);
        stubClock.setNextDate(TODAY);
        receiver.acceptRequest(FIRST_REQUEST);
        stubClock.setNextDate(TOMORROW);
        assertFalse("too late now", receiver.acceptRequest(SECOND_REQUEST));
    }
}
