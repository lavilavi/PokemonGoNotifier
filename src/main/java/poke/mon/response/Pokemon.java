package poke.mon.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import poke.mon.Coord;
import poke.mon.dex.Pokedex;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;

/**
 * @author clerc
 * @since 2016/07/29
 */
public class Pokemon {
    private static final DateFormat SIMPLE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private static final int MAX_DIST = 700;
    private static final int ALREADY_DEAD = 10;

    public long id;
    public String data;
    public long expiration_time;
    public int pokemonId;
    public double latitude;
    public double longitude;
    public String uid;
    public boolean is_alive;

    @JsonIgnore
    private Pokedex pokedex;

    @JsonIgnore
    public Rarity level() {
        switch (makeName()) {
            case "Rattata":
            case "Zubat":
            case "Magikarp":
            case "Psyduck":
            case "Pidgey":
            case "Weedle":
            case "Paras":
            case "Kakuna":
            case "Ekans":
            case "Caterpie":
            case "Doduo":
            case "Dodrio":
            case "Tentacool":
            case "Spearow":
            case "Raticate":
            case "Pidgeot":
            case "Venonat":
            case "Venomoth":
            case "Krabby":
            case "Kingler":
            case "Pidgeotto":
            case "Golbat":
            case "Staryu":
            case "Slowpoke":
                return Rarity.DONT_CARE;
            case "Exeggcute":
            case "Poliwag":
                return Rarity.NOT_BAD;
            case "Golduck":
            case "Slowbro":
            case "Horsea":
            case "Goldeen":
            case "Meoth":
            case "Eevee":
            case "Hitmonchan":
            case "Machop":
            case "Voltorb":
            case "Dratini":
            case "Dragonair":
            case "Jynx":
            case "Bellsprout":
            case "Cubone":
            case "Magnemite":
            case "Meowth":
            case "Farfetch'd":
                return Rarity.COOL;
            default:
                return Rarity.UNKNOWN;
        }
    }

    @JsonIgnore
    public boolean toIgnore(Coord coords) {
        return !is_alive ||
                (distance(coords) > timeLeftInSec() * 2) ||
                distance(coords) > MAX_DIST ||
                timeLeftInSec() < ALREADY_DEAD;
    }

    @JsonIgnore
    public String makeName() {
        return pokedex.get(pokemonId).getName();
    }

    @JsonIgnore
    public void pokedex(Pokedex pokedex) {
        this.pokedex = pokedex;
    }

    @JsonIgnore
    public long timeLeftInSec() {
        long now = Instant.now().toEpochMilli() / 1000;
        return expiration_time - now;
    }

    @JsonIgnore
    public String timeLeft() {
        long time = timeLeftInSec();
        return String.format("%d:%02d", time / 60, time % 60);
    }

    @JsonIgnore
    public int distance(Coord coords) {
        return (int) Math.round(
                gps2m(coords.latitude, coords.longitude, latitude, longitude) / 100
        ) * 100;
    }

    @JsonIgnore
    private double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
        double pk = (180.0 / Math.PI);

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getExpiration_time() {
        return expiration_time;
    }

    public void setExpiration_time(long expiration_time) {
        this.expiration_time = expiration_time;
    }

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean is_alive() {
        return is_alive;
    }

    public void setIs_alive(boolean is_alive) {
        this.is_alive = is_alive;
    }

    @JsonIgnore
    public String getExpirationDate() {
        return SIMPLE_FORMAT.format(new Date(expiration_time * 1000));
    }

    @JsonIgnore
    public String getImage() {
        String nameForUrl = makeName();
        nameForUrl = nameForUrl.toLowerCase(Locale.ENGLISH);
        nameForUrl = nameForUrl.replaceAll("'", "");

        return "https://img.pokemondb.net/artwork/" + nameForUrl + ".jpg";
    }

    @JsonIgnore
    public String getMap() {
        return "http://www.openstreetmap.org/?mlat=" + latitude + "%26mlon=" + longitude +
                "#map=16/" + latitude + "/" + longitude;
    }
}
