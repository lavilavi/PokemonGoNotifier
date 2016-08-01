package poke.mon.exceptions;

/**
 * @author clerc
 * @since 2016/07/29
 */
public class ReaderException extends Exception {
    public ReaderException(Exception e) {
        super("Error accessing poke finder", e);
    }

    public ReaderException(String cause) {
        super("Error accessing poke finder: " + cause);
    }
}
