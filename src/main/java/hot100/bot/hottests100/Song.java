package hot100.bot.hottests100;

public class Song {
    private String name;
    private String artist;

    public Song(String artist, String name) {
        this.name = name;
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return artist + " \n " + name;
    }
}

