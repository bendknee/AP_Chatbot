package advprog.example.bot.NewAgeBot;

import java.util.HashMap;
import java.util.List;

public class NewAgeBot {

    List<NewAgeAlbum> chart;
    String find;
    String url;

    public List<NewAgeAlbum> getChart(){
        return null;
    }

    public boolean isExist() {
        for(NewAgeAlbum a : chart) {
            if(a.getArtist().toLowerCase().contains(find.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    public String getFavArtist(){

    }

    public String getUrl() {

    }

}