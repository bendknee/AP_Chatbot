package advprog.example.bot.HotCountryBot;

public class Song {
    private String title;
    private String artist;

    public Song(String title, String artist) {
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
        return artist + " - " + name;
    }
}
