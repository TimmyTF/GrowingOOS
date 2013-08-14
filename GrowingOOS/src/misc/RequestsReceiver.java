package misc;

import java.util.Date;

/**
 * User: tflomin
 * Date: 14.08.13
 * Time: 16:27
 */
public class RequestsReceiver {
    private Date dateOfFirstRequest = new Date(1234567891011L);
    private Clock clock;
//    private Date dateOfFirstRequest = null;

    public RequestsReceiver() {
        // TODO
    }

    public RequestsReceiver(Clock clock) {
        this.clock = clock;
    }

    public boolean acceptRequest(Request request) {
        final Date now = clock.now();
        if (dateOfFirstRequest == null) {
            dateOfFirstRequest = now;
        } else if (firstDateIsDifferentFrom(now)) {
            return false;
        }

        // process the request:
        request.beingProcessed();

        return true;
    }

    private boolean firstDateIsDifferentFrom(Date thatDate) {
        return dateOfFirstRequest.compareTo(thatDate) != 0;
    }
}
