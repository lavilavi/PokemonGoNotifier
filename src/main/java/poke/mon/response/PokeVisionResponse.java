package poke.mon.response;

import java.util.List;

/**
 * @author clerc
 * @since 2016/07/29
 */
public class PokeVisionResponse implements ReaderResponse {
    public String status;
    public List<Pokemon> pokemon;

    @Override
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public List<Pokemon> getPokemon() {
        return pokemon;
    }

    public void setPokemon(List<Pokemon> pokemon) {
        this.pokemon = pokemon;
    }
}
