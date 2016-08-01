package poke.mon.dex;

/**
 * @author clerc
 * @since 2016/07/29
 */
public class PokedexEntry {
    public int id;

    public String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
