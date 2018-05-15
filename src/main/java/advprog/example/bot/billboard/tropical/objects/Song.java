package advprog.example.bot.billboard.tropical.objects;

public class Song {
    private String author;
    private int rank;
    private String song;

    public Song(int rank, String author, String song) {
        this.rank = rank;
        this.author = author;
        this.song = song;
    }

    public String getAuthor() {
        return author;
    }

    public int getRank() {
        return rank;
    }

    public String getSong() {
        return song;
    }

    public String toString() {
        return "(" + rank + ") " + author + " - " + song;
    }
}
