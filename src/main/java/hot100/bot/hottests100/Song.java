package hot100.bot.hottests100;

public class Song {
    private String artists;

    public Song(String artists) {
        this.artists = artists;
    }


    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }
    @Override
    public String toString() {
        return artists;
    }
}
