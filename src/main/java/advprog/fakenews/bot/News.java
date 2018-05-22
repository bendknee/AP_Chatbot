package advprog.fakenews.bot;

public class News {
    String newsUrl, type, type2, type3, newsNotes;

    public News(String newsUrl, String type, String type2, String type3, String newsNotes) {
        this.newsUrl = newsUrl;
        this.type = type;
        this.type2 = type2;
        this.type3 = type3;
        this.newsNotes = newsNotes;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getType3() {
        return type3;
    }

    public void setType3(String type3) {
        this.type3 = type3;
    }

    public String getNewsNotes() {
        return newsNotes;
    }

    public void setNewsNotes(String newsNotes) {
        this.newsNotes = newsNotes;
    }

    public boolean containsFilter(String criteria){
        if (this.getType().equalsIgnoreCase(criteria) || this.getType2()
                .equalsIgnoreCase(criteria) || this.getType3().equalsIgnoreCase(criteria)){
            return true;
        } else {
            return false;
        }
    }
}