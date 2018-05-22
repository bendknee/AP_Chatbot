package advprog.example.bot.feature;

// // This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

public class Enterkomputer {

    public static String findPrice(String category, String name) {
        String apiUrl = "https://www.enterkomputer.com//api/product/" + category + ".json";
        try {
            HttpResponse<JsonNode> response = Unirest.get(apiUrl).asJson();
            StringBuilder out = new StringBuilder();
            response.getBody().getArray().forEach(item -> {
                JSONObject itemJson = ((JSONObject)item);
                if (itemJson.getString("name").toLowerCase().contains(name)) {
                    String itemName = itemJson.getString("name");
                    String itemPrice = itemJson.getString("price");
                    out.append(itemName + " - " + itemPrice + "\n");
                }
            });
            if (out.toString().equals("")) {
                return "No related items";
            } else {
                return out.toString().substring(0,out.length() - 1);
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return "Not found";
    }
}