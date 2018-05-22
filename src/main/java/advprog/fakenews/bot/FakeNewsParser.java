package advprog.fakenews.bot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FakeNewsParser {
    private List<News> newsList;
    private List<String> criteria;

    public FakeNewsParser() {
        criteria = Arrays.asList("fake", "political", "satire",
                "unreliable", "bias", "conspiracy");
        readData();
    }

    public void readData() {
        try {
            Type listType = new TypeToken<List<News>>(){}.getType();
            Gson gson = new Gson();
            File file = new File("news-source.json");
            BufferedReader br = new BufferedReader(new FileReader(file));
            JsonElement element = new JsonParser().parse(br);
            //Type newsType = (Type) new TypeToken<List<News>>(){}.getType();
            //Type collectionType = new TypeToken<List<News>>(){}.getType();
            //newsList = gson.fromJson(element, new TypeToken<List<News>>(){}.getType());
            newsList = gson.fromJson(br, listType);
        } catch (FileNotFoundException e) {
            System.out.println("File Tidak Ditemukan");
        }
    }

    public News checkListContainsNews(String newsName) {
        return newsList.stream().filter(news -> news.getNewsUrl().equals(newsName))
                .findAny().orElse(null);
    }

    public String addNewCriteria(String news, String criteria) {
        News targetNews = checkListContainsNews(news);
        if (targetNews == null) {
            return "url " + news + " is not present";
        } else if (targetNews.containsFilter(criteria)) {
            return criteria + " is already present";
        } else if (this.criteria.contains(criteria)) {
            if (targetNews.getNewsType2().equals("")) {
                targetNews.setNewsType2(criteria);
                writeToJson();
                return criteria + " added as a criteria of " + news;
            } else if (targetNews.getNewsType3().equals("")) {
                targetNews.setNewsType3(criteria);
                writeToJson();
                return criteria + " added as a criteria of " + news;
            } else {
                return "criteria field is full";
            }
        } else {
            return criteria + " is not a valid criteria";
        }
    }

    private void writeToJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter writer = new FileWriter("news-source.json");
            writer.write(gson.toJson(newsList));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String printWarning(String news) {
        return checkListContainsNews(news) != null ? "warning "
                + news + " considered as "
                + checkListContainsNews(news).getType() : "";
    }

    public String checkNews(String news, String criteria) {
        News targetNews = checkListContainsNews(news);
        if (targetNews == null) {
            return news + " is safe";
        } else if (!targetNews.containsFilter(criteria)) {
            return news + " is not considered as " + criteria;
        } else if (targetNews.containsFilter(criteria)) {
            return news + " is considered as " + criteria;
        } else {
            return "input not valid";
        }
    }

    public static void main(String[] args) {
        FakeNewsParser y = new FakeNewsParser();
        String x = "2";
        y.checkListContainsNews("365usanews.com");
    }
}
