package poke.mon.universe;

import poke.mon.Coord;
import poke.mon.logger.SlackLogger;
import poke.mon.logger.SlackMessage;
import poke.mon.response.Pokemon;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author clerc
 * @since 2016/08/01
 */
public class Universe {
    private final List<Pokemon> pokemonList = new LinkedList<>();
    private final Coord coords;

    private static final DateFormat FULL_FORMAT = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");

    public Universe(Coord coords) {
        this.coords = coords;
    }

    public void cleanup() {
        Iterator<Pokemon> iter = pokemonList.iterator();
        while (iter.hasNext()) {
            Pokemon pokemon = iter.next();
            if (pokemon.toIgnore(coords)) {
                iter.remove();

                SlackLogger.log(new SlackMessage(pokemon, false));
            }
        }
    }

    public void add(Pokemon pokemon) {
        if (isNew(pokemon)) {
            SlackLogger.log(new SlackMessage(pokemon, true));

            pokemonList.add(pokemon);
        }
    }

    private boolean isNew(Pokemon newPoke) {
        for (Pokemon oldPoke : pokemonList) {
            if (samePokemon(oldPoke, newPoke)) {
                return false;
            }
        }
        return true;
    }

    private boolean samePokemon(Pokemon oldPoke, Pokemon newPoke) {
        return newPoke.getPokemonId() == oldPoke.getPokemonId() &&
                newPoke.getLongitude() == oldPoke.getLongitude() &&
                newPoke.getLatitude() == newPoke.getLatitude();

    }
}
