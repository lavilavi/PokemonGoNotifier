package poke.mon.logger;

import poke.mon.Coord;
import poke.mon.response.Pokemon;
import poke.mon.response.Rarity;

/**
 * @author clerc
 * @since 2016/08/01
 */
public class SlackAttachment {
    private String author_name;
    private String author_link;
    private String color;
    private String text;
    private String thumb_url;

    public SlackAttachment(Pokemon pokemon, boolean created) {
        Coord coords = new Coord();
        if (created) {
            this.text = "";
            if (pokemon.level() == Rarity.COOL || pokemon.level() == Rarity.UNKNOWN) {
                this.text += "@here ";
            }
            this.text += " Disappears at: " + pokemon.getExpirationDate() + "\n";
            this.text += "Remaining: " + pokemon.timeLeft() + " Distance: " + pokemon.distance(coords) + "m\n";

            this.color = colorFor(pokemon.level());
        } else {
            this.text = pokemon.makeName() + " has  left";
            this.color = "#ff0000";
        }
        this.thumb_url = pokemon.getImage();
        this.author_name = pokemon.makeName();
        this.author_link = pokemon.getMap();
    }

    private String colorFor(Rarity level) {
        switch (level) {
            case NOT_BAD:
                return "#36a64f";
            case COOL:
                return "#5a55ff";
            default:
                return "#ffb640";
        }
    }

    public String getColor() {
        return color;
    }

    public String getText() {
        return text;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public String getAuthor_link() {
        return author_link;
    }
}
