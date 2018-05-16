package advprog.example.bot.feature;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;

import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class PicAnalyze {

    public static boolean flag = false;

    public static String analyze(String imageUrl) {
        flag = false;
        HttpClient httpclient = HttpClients.createDefault();

        try {
            URIBuilder builder = new URIBuilder("https://southeastasia.api.cognitive.microsoft.com/vision/v1.0/analyze");

            builder.setParameter("visualFeatures", "Categories");
            builder.setParameter("language", "en");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", "6c6d2bacea154229bb8972c7c33303cb");


            // Request body
            StringEntity reqEntity = new StringEntity("{\"url\":\"" + imageUrl + "\"}");
            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                return (EntityUtils.toString(entity));
            }
            return "no entity";
        } catch (Exception e) {
            return ("error");
        }
    }
}