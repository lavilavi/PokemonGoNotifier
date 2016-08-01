package poke.mon.logger;

import poke.mon.response.Pokemon;

import java.util.LinkedList;
import java.util.List;

/**
 * @author clerc
 * @since 2016/08/01
 */
public class SlackMessage {
    private final List<SlackAttachment> attachments = new LinkedList<>();
    private String text;

    public SlackMessage(Pokemon pokemon, boolean created) {
        this.attachments.add(new SlackAttachment(pokemon, created));
    }

    public List<SlackAttachment> getAttachments() {
        return attachments;
    }

    public String getText() {
        return text;
    }
}
