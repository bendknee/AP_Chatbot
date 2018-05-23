package advprog.fakenews.bot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FakeNewsParser {
    public List<News> newsList;
    private List<String> criteria;

    public FakeNewsParser() {
        criteria = Arrays.asList("fake", "political", "satire",
                "unreliable", "bias", "conspiracy");
        readData();
    }

    public void readData() {
        try {
            Type listType = new TypeToken<ArrayList<News>>(){}.getType();
            Gson gson = new Gson();
            File file = new File("news.json");
            BufferedReader br = new BufferedReader(new FileReader(file));
            JsonElement element = new JsonParser().parse(br);
            newsList = gson.fromJson(element, new TypeToken<List<News>>(){}.getType());
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
            if (targetNews.getType2().equals("")) {
                targetNews.setType2(criteria);
                writeToJson();
                return criteria + " added as a criteria of " + news;
            } else if (targetNews.getType3().equals("")) {
                targetNews.setType3(criteria);
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
            FileWriter writer = new FileWriter("news.json");
            writer.write(gson.toJson(newsList));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String printWarning(String news) {
        return checkListContainsNews(news) != null ? "Dangerous!!!! "
                + news + " considered as "
                + checkListContainsNews(news).getType() : "";
    }

    public String checkNews(String news, String criteria) {
        News targetNews = checkListContainsNews(news);
        if (targetNews == null) {
            return news + " is a safe news website";
        } else if (!targetNews.containsFilter(criteria)) {
            return news + " isn't a " + criteria + " news website";
        } else if (targetNews.containsFilter(criteria)) {
            return news + " is a " + criteria + " news website";
        } else {
            return "input not valid";
        }
    }

    public static void main(String[] args) {
        FakeNewsParser y = new FakeNewsParser();
    }
}
