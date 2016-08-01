package poke.mon.reader;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import poke.mon.Coord;
import poke.mon.exceptions.ReaderException;
import poke.mon.response.PGoSearchReaderResponse;
import poke.mon.response.Pokemon;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author clerc
 * @since 2016/08/01
 */
public class PGoSearchReader implements Reader {
    @Override
    public PGoSearchReaderResponse read(Coord coords) throws ReaderException {
        PGoSearchReaderResponse readerResponse = new PGoSearchReaderResponse();
        try {
            URI uri = new URIBuilder()
                    .setScheme("https")
                    .setHost("sv-db2.pmap.kuku.lu")
                    .setPath("/_dbserver.php")
                    .setParameter("action", "viewData")
                    .setParameter("sv", "sv-vps135.pmap.kuku.lu")
                    .setParameter("research_key", "e4678236-79ca-45a1-93eb-6b528a1bf0be")
                    .setParameter("loc1", "" + coords.latitude)
                    .setParameter("loc2", "" + coords.longitude)
                    .setParameter("_", "1470014964519")
                    .build();
            HttpGet httpget = new HttpGet(uri);
            httpget.addHeader("Origin", "https://pmap.kuku.lu");
            httpget.addHeader("Accept-Encoding", "gzip, deflate, sdch, br");
            httpget.addHeader("Accept-Language", "en,en-US;q=0.8,ja;q=0.6,fr;q=0.4");
            httpget.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36");
            httpget.addHeader("Accept", "*/*");
            httpget.addHeader("Referer", "https://pmap.kuku.lu/");
            httpget.addHeader("Connection", "keep-alive");

            CloseableHttpClient httpclient = HttpClients.createDefault();
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String output = convertStreamToString(instream);
                    for (String line : output.split("\n")) {
                        if (!line.trim().isEmpty()) {
                            Pokemon pokemon = new Pokemon();
                            pokemon.setIs_alive(true);

                            String[] parts = line.split(";");
                            for (String part : parts) {
                                if (!part.trim().isEmpty()) {
                                    String[] kv = part.split("=");
                                    switch (kv[0]) {
                                        case "action":
                                        case "distance":
                                            //ignore token
                                            break;
                                        case "id":
                                            pokemon.setPokemonId(Integer.parseInt(kv[1]));
                                            break;
                                        case "loc":
                                            pokemon.setLatitude(Double.parseDouble(kv[1].split(",")[0]));
                                            pokemon.setLongitude(Double.parseDouble(kv[1].split(",")[1]));
                                            break;
                                        case "tol":
                                            pokemon.setExpiration_time(Long.parseLong(kv[1])/1000);
                                            break;
                                        default:
                                            System.out.println("Unknown token:" + kv[0]);
                                    }
                                }
                            }
                            readerResponse.addPokemon(pokemon);
                        }
                    }
                } else {
                    throw new ReaderException("No response");
                }
            } finally {
                response.close();
            }

        } catch (URISyntaxException | IOException e) {
            throw new ReaderException(e);
        }

        return readerResponse;
    }

    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
