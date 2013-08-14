package auctionsniper.util;

/**
 * User: tflomin
 * Date: 14.08.13
 * Time: 10:39
 */
public class Defect extends RuntimeException {
    public Defect() {
        super();
    }

    public Defect(String message, Throwable cause) {
        super(message, cause);
    }

    public Defect(String message) {
        super(message);
    }

    public Defect(Throwable cause) {
        super(cause);
    }

}
