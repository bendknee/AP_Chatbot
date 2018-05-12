package hot100.bot.hottests100;

public class Song {
    private String name;
    private String artists;

    public Song(String name, String artists) {
        this.name = name;
        this.artists = artists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }
    @Override
    public String toString() {
        return artists + " - " + name;
    }
}
