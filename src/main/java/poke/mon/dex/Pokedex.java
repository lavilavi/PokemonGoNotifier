package poke.mon.dex;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author clerc
 * @since 2016/07/29
 */
public class Pokedex {
    private static final ObjectMapper mapper = new ObjectMapper();
    private final PokeList pokeList;

    public Pokedex(String filePath) throws IOException {
        this.pokeList = mapper.convertValue(
                mapper.reader().readTree(
                        String.join(
                                "\n",
                                tryRead(Paths.get(filePath))
                        )
                ),
                PokeList.class
        );
    }

    private List<String> tryRead(Path file) throws IOException {
        try {
            return Files.readAllLines(file, Charset.forName("SJIS"));
        } catch (IOException e) {
            return Files.readAllLines(file, Charset.forName("UTF-8"));
        }
    }

    public PokedexEntry get(int pokemonId) {
        return pokeList.get(pokemonId - 1);
    }
}
