package poke.mon.response;

import java.util.LinkedList;
import java.util.List;

/**
 * @author clerc
 * @since 2016/08/01
 */
public class PGoSearchReaderResponse implements ReaderResponse {
    private final List<Pokemon> pokemonList = new LinkedList<>();

    public void addPokemon(Pokemon pokemon) {
        pokemonList.add(pokemon);
    }

    @Override
    public List<Pokemon> getPokemon() {
        return pokemonList;
    }

    @Override
    public String getStatus() {
        return "success";
    }
}
