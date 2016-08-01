package poke.mon.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import poke.mon.Coord;
import poke.mon.exceptions.ReaderException;
import poke.mon.response.PokeVisionResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author clerc
 * @since 2016/07/29
 */
public class PokevisionReader implements Reader{
    private static final String COOKIE = "__cfduid=db39a1c0f56d2ee5bd29d0f16c8fd03991469429174; app-session=4a2v3eoqpcfupug45n53ba4q12; cdmu=1469754287603; cdmtlk=0:0:0:0:0:0:0:0:0:0:0:0; cdmgeo=us; cdmbaserate=2.1; cdmbaseraterow=1.1; cdmint=0; cdmblk2=0:0:0:0:0:0:0:0:0:0:0:0,0:0:0:0:0:0:0:0:0:0:0:0,0:0:0:0:0:0:0:0:0:0:0:0,0:0:0:0:0:0:0:0:0:0:0:0,0:0:0:0:0:0:0:0:0:0:0:0,0:0:0:0:0:0:0:0:0:0:0:0; cdmblk=0:0:0:0:0:0:0:0:0:0:0:0,0:0:0:0:0:0:0:0:0:0:0:0,0:0:0:0:0:0:0:0:0:0:0:0,0:0:0:0:0:0:0:0:0:0:0:0,0:0:0:0:0:0:0:0:0:0:0:0,0:0:0:0:0:0:0:0:0:0:0:0";

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public PokeVisionResponse read(Coord coords) throws ReaderException {
        try {
            URI uri = new URIBuilder()
                    .setScheme("https")
                    .setHost("pokevision.com")
                    .setPath("/map/data/"+coords.latitude+"/"+coords.longitude)
                    .setParameter("q", "httpclient")
                    .setParameter("btnG", "Google Search")
                    .setParameter("aq", "f")
                    .setParameter("oq", "")
                    .build();
            HttpGet httpget = new HttpGet(uri);
            httpget.addHeader("accept-encoding", "gzip, deflate, sdch, br");
            httpget.addHeader("x-requested-with", "XMLHttpRequest");
            httpget.addHeader("accept-language", "en,en-US;q=0.8,ja;q=0.6,fr;q=0.4");
            httpget.addHeader("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36");
            httpget.addHeader("accept", "application/json, text/javascript, */*; q=0.01");
            httpget.addHeader("referer", "https://pokevision.com/");
            httpget.addHeader("authority", "pokevision.com");
            httpget.addHeader("cookie", COOKIE);

            CloseableHttpClient httpclient = HttpClients.createDefault();
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String output = convertStreamToString(instream);
                    return mapper.convertValue(
                            mapper.reader().readTree(
                                    output
                            ),
                            PokeVisionResponse.class
                    );
                }else{
                    throw new ReaderException("No response");
                }
            } finally {
                response.close();
            }

        } catch (URISyntaxException | IOException e) {
            throw new ReaderException(e);
        }
    }

    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
