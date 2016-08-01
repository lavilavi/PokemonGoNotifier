package poke.mon.response;

import java.util.List;

/**
 * @author clerc
 * @since 2016/08/01
 */
public interface ReaderResponse {
    List<Pokemon> getPokemon();

    String getStatus();
}
