package advprog.fakenews.bot;

public class News {
    String newsUrl, newsType1, newsType2, newsType3, newsNotes;

    public News(){

    }

    public News(String newsUrl, String newsType1, String newsType2, String newsType3, String newsNotes) {
        this.newsUrl = newsUrl;
        this.newsType1 = newsType1;
        this.newsType2 = newsType2;
        this.newsType3 = newsType3;
        this.newsNotes = newsNotes;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getNewsType1() {
        return newsType1;
    }

    public void setNewsType1(String newsType1) {
        this.newsType1 = newsType1;
    }

    public String getNewsType2() {
        return newsType2;
    }

    public void setNewsType2(String newsType2) {
        this.newsType2 = newsType2;
    }

    public String getNewsType3() {
        return newsType3;
    }

    public void setNewsType3(String newsType3) {
        this.newsType3 = newsType3;
    }

    public String getNewsNotes() {
        return newsNotes;
    }

    public void setNewsNotes(String newsNotes) {
        this.newsNotes = newsNotes;
    }

    public boolean containsFilter(String criteria){
        return true;
        //return false;
    }

    public String getType(){
        return "x";
    }
}