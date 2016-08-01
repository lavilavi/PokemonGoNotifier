package poke.mon;

import poke.mon.dex.Pokedex;
import poke.mon.exceptions.ReaderException;
import poke.mon.reader.PGoSearchReader;
import poke.mon.reader.Reader;
import poke.mon.response.Pokemon;
import poke.mon.response.Rarity;
import poke.mon.response.ReaderResponse;
import poke.mon.universe.Universe;

import java.io.IOException;

/**
 * @author clerc
 * @since 2016/07/29
 */
public class Main {
    public static void main(String... args) {
        System.setProperty("jsse.enableSNIExtension", "false");

        Coord coords = new Coord();

        try {
            Pokedex pokedex = new Pokedex("/d/script/mon/pokelist.json");
            Universe universe = new Universe(coords);

            while (!Thread.currentThread().isInterrupted()) {
                universe.cleanup();

                //Reader reader = new PokevisionReader();
                Reader reader = new PGoSearchReader();
                ReaderResponse response = reader.read(coords);

                if ("success".equals(response.getStatus())) {
                    for (Pokemon pokemon : response.getPokemon()) {
                        if (!pokemon.toIgnore(coords)) {
                            pokemon.pokedex(pokedex);

                            if (pokemon.level() != Rarity.DONT_CARE) {
                                universe.add(pokemon);
                            }
                        }
                    }
                } else {
                    System.out.println("Failure from remote");
                }
                Thread.sleep(60000);
            }
        } catch (ReaderException | IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
