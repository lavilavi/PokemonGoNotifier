package poke.mon.reader;

import poke.mon.Coord;
import poke.mon.exceptions.ReaderException;
import poke.mon.response.ReaderResponse;

/**
 * @author clerc
 * @since 2016/08/01
 */
public interface Reader {
    ReaderResponse read(Coord coords) throws ReaderException;
}
