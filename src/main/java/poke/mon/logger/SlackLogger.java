package poke.mon.logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author clerc
 * @since 2016/08/01
 */
public class SlackLogger {
    private static final String URL = "https://hooks.slack.com/services/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";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Integer log(SlackMessage message) {
        try {
            String body = objectMapper.writeValueAsString(message);

            try {
                HttpURLConnection con = (HttpURLConnection) new URL(URL).openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                con.getOutputStream().write((
                        "payload=" + body
                ).getBytes("UTF-8"));
                int code = con.getResponseCode();
                if (code != 200) {
                    System.out.println("Slack response code " + code + " with message " + con.getResponseMessage() + " while sending \n" + body);
                }
                return code;
            } catch (IOException e) {
                System.out.println("Exception  while sending \n" + body);
                e.printStackTrace();
            }
            return null;
        } catch (JsonProcessingException e) {
            System.out.println("Could not jsonify" + message);
        }
        return null;
    }
}
