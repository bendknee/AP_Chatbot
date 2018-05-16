package com.bot.botapakah;
import ch.qos.logback.core.net.server.Client;
import com.bot.Getter.Getter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.xml.bind.DatatypeConverter;
import java.util.logging.Logger;

import static java.sql.DriverManager.println;

@SpringBootApplication
@LineMessageHandler
public class BotApakahApplication extends SpringBootServletInitializer {
    Client client;
    String data;
    String[] myData;
    String myDataTags;

    private static final Logger LOGGER = Logger.getLogger(BotApakahApplication.class.getName());
    private static final String UPLOAD_API_URL = "https://api.imgur.com/3/image";
    private static final String CLIENT_ID = "238fac3ff34afe4";

    //CHANGE TO @CLIENT_ID@ and replace with buildscript.


    @Autowired
    private LineMessagingClient lineMessagingClient;

    private static String AccessToken = "Access Token here";
    Getter getter = new Getter(AccessToken);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BotApakahApplication.class);
    }

    public static void main(String[] args) {

        SpringApplication.run(BotApakahApplication.class, args);
    }

    private static void downloadFromApi(String arg) throws IOException {
        String saveDir = System.getProperty("user.dir");
        final int BUFFER_SIZE = 4096;
        String channelToken = "noisYhDmE7nZtWJwc2Z0hkr9IPv6Sgw/YNrhyX92ztPeMcjnJQmr27TR2ebk8t00NJg4OBp0Y890hkN1zEJGE2tY94bZJn3l5IEOZXnKg/6nEI0uQWU3mdmOgm/R0Ml7FizuBiFOtz/DyMF/3uwfjAdB04t89/1O/w1cDnyilFU=";
        String urll = "https://api.line.me/v2/bot/message/"+arg+"/content";
        URL url = new URL(urll);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestProperty("Authorization", "Bearer " + channelToken);
        int responseCode = httpConn.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");

            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = "tempFile.jpg";
            }

            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = saveDir + File.separator + fileName;

            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            LOGGER.info("File Downloaded");
            System.out.println("File downloaded");
        } else {
            LOGGER.info("File Not Downloaded");
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
    }

    public static String uploadToImgur() throws JSONException{
        String filepath = System.getProperty("user.dir")+"/tempFile.jpg";
        File img = new File(filepath);
        JSONObject tmp = new JSONObject(upload(img));
        JSONObject data = (JSONObject) tmp.get("data");
        String url = (String) data.get("link");
        return url;
    }

    public String nembakApiDefault(String input) throws IOException, JSONException {
        String credentialsToEncode = "acc_5923548cfcdb7aa" + ":" + "b847a86333f34d3d417f456283b9462d";
        String basicAuth = java.util.Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));
        String endpoint_url = "https://api.imagga.com/v1/colors";
        String image_url = input;

        String url = endpoint_url + "?url=" + image_url;
        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        connection.setRequestProperty("Authorization", "Basic " + basicAuth);

        int responseCode = connection.getResponseCode();

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String jsonResponse = connectionInput.readLine();

        connectionInput.close();

        System.out.println(jsonResponse);

        return getta(jsonResponse);

    }
    public String nembakApiGambar(String input) throws IOException, JSONException {
        downloadFromApi(input);
        String credentialsToEncode = "acc_5923548cfcdb7aa" + ":" + "b847a86333f34d3d417f456283b9462d";
        String basicAuth = java.util.Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));
        String endpoint_url = "https://api.imagga.com/v1/colors";
        String image_url = uploadToImgur();

        String url = endpoint_url + "?url=" + image_url;
        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        connection.setRequestProperty("Authorization", "Basic " + basicAuth);

        int responseCode = connection.getResponseCode();

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String jsonResponse = connectionInput.readLine();

        connectionInput.close();

        System.out.println(jsonResponse);

        return getta(jsonResponse);

    }

    public String getta(String inputjson) throws JSONException {

        JSONObject json = new JSONObject(inputjson);
        String percentagefg = json.getJSONArray("results").getJSONObject(0).getJSONObject("info").getJSONArray("foreground_colors").getJSONObject(0).getString("percentage");
        String dominantfg = json.getJSONArray("results").getJSONObject(0).getJSONObject("info").getJSONArray("foreground_colors").getJSONObject(0).getString("closest_palette_color");
        String dominant = json.getJSONArray("results").getJSONObject(0).getJSONObject("info").getJSONArray("background_colors").getJSONObject(0).getString("closest_palette_color");
        String percentage = json.getJSONArray("results").getJSONObject(0).getJSONObject("info").getJSONArray("background_colors").getJSONObject(0).getString("percentage");
        String hasil = "Color paling banyak background: " + dominant + " dengan hasil : " + percentage + "%" + "\n" +"Color paling banyak foreground: " + dominantfg + " dengan hasil : " + percentagefg + "%";
        return hasil;
    }




//    public String nembakApi(String id) {
//
//
//        String basic = getBasicAuth("acc_5923548cfcdb7aa", "b847a86333f34d3d417f456283b9462d");
//        RestTemplate restTemplate = new RestTemplate();
//        //String url = id;
//        String url = "https://api.line.me/v2/bot/message/"+id+"/content";
//
//        UriComponentsBuilder uri = UriComponentsBuilder
//                .fromUriString("https://api.imagga.com/v1/colors")
//                .queryParam("url", url);
//        LOGGER.info(uri.toUriString());
//
//        HttpHeaders header = new HttpHeaders();
//        header.add("Authorization", basic);
//
//        HttpEntity<String> request = new HttpEntity<String>(String.valueOf(header));
//        LOGGER.info(String.valueOf(request));
//        // String hasil = json.getJSONArray("results").getJSONObject(0).getJSONArray("categories").getJSONObject(0).getString("name");
//        ResponseEntity<String> response = restTemplate.exchange(uri.toUriString(), HttpMethod.GET, request, String.class);
//        LOGGER.info(response.getBody());
//        return response.getBody();
//        //jsonnode
//    }
//
//    static String getBasicAuth(String username, String password) {
//        String auth = username + ":" + password;
//        String auth64 = new String(Base64.encodeBase64(auth.getBytes()));
//        LOGGER.info("THIS IS");
//
//        return "Basic " + auth64;
//    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    class Response {
        Result[] results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class Result {
        Info info;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class Info {
        Color[] background_colors;
        Color[] foreground_colors;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class Color {
        String closest_palette_color_html_code;
        double percentage;
    }

    class Context{
        private HashMap<String,String> penyimpan;

    }

    @EventMapping
    public void handleTextEvent(MessageEvent<TextMessageContent> messageEvent) throws IOException, JSONException {
        String pesan = messageEvent.getMessage().getText();
        String jawaban = nembakApiDefault(pesan);
        String replyToken = messageEvent.getReplyToken();
        balasChatDenganRandomJawaban(replyToken,jawaban);
    }
    @EventMapping
    public void handleImageMessageEvent(MessageEvent<ImageMessageContent> imageEvent) throws IOException, JSONException {
        String imageid = imageEvent.getMessage().getId();
        String replyToken = imageEvent.getReplyToken();
        String jawaban = nembakApiGambar(imageid);
        downloadFromApi(imageid);
        balasChatDenganRandomJawaban(replyToken,jawaban);
    }

    private String getRandomJawaban(){
        String jawaban = "";
        int random = new Random().nextInt();
        if(random%2==0){
            jawaban = "Ya";
        } else{
            jawaban = "Nggak";
        }
        return jawaban;
    }


    private void balasChatDenganRandomJawaban(String replyToken, String jawaban){
        TextMessage jawabanDalamBentukTextMessage = new TextMessage(jawaban);
        try {
            lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, jawabanDalamBentukTextMessage))
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Ada error saat ingin membalas chat");
        }
    }
    public static String upload(File file) {
        HttpURLConnection conn = getHttpConnection(UPLOAD_API_URL);
        writeToConnection(conn, "image=" + toBase64(file));
        return getResponse(conn);
    }


    /**
     * Converts a file to a Base64 String.
     *
     * @param file The file to be converted.
     * @return The file as a Base64 String.
     */
    public static String toBase64(File file) {
        try {
            byte[] b = new byte[(int) file.length()];
            FileInputStream fs = new FileInputStream(file);
            fs.read(b);
            fs.close();
            return URLEncoder.encode(DatatypeConverter.printBase64Binary(b), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Creates and sets up an HttpURLConnection for use with the Imgur API.
     *
     * @param url The URL to connect to. (check Imgur API for correct URL).
     * @return The newly created HttpURLConnection.
     */
    public static HttpURLConnection getHttpConnection(String url) {
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);
            conn.setReadTimeout(100000);
            conn.connect();
            return conn;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Sends the provided message to the connection as uploaded data.
     *
     * @param conn    The connection to send the data to.
     * @param message The data to upload.
     */
    public static void writeToConnection(HttpURLConnection conn, String message) {
        OutputStreamWriter writer;
        try {
            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(message);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Gets the response from the connection, Usually in the format of a JSON string.
     *
     * @param conn The connection to listen to.
     * @return The response, usually as a JSON string.
     */
    public static String getResponse(HttpURLConnection conn) {
        StringBuilder str = new StringBuilder();
        BufferedReader reader;
        try {
            if (conn.getResponseCode() != StatusCode.SUCCESS.getHttpCode()) {
                throw new RuntimeException();
            }
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                str.append(line);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
        if (str.toString().equals("")) {
            throw new RuntimeException();
        }
        return str.toString();
    }

    public enum StatusCode {
        UNKNOWN_HOST("Couldn't find api.imgur.com, are you connected to the internet?", 1),
        SUCCESS("The action was successful!", 200),
        BAD_REQUEST("Upload interupted or corrupted.", 400),
        UNAUTHORIZED("Action requires Auth. Credentials are invalid.", 401),
        FORBIDDEN("You don't have access.  Possible lack of API credits.", 403),
        NOT_FOUND("The requested image or album does not exist.", 404),
        FILE_TOO_BIG("The file that you tried to upload was too big!", 413),
        UPLOAD_LIMITED("You are uploading to quickly! You've been rate limited.", 429),
        INTERNAL_SERVER_ERROR("Imgur unexpected internal server error. Not our fault.", 500),
        SERVICE_UNAVAILABLE("Imgur is unavailable currently.  Most likely over capacity.", 502),
        UNKNOWN_ERROR("An error occured, but we don't know what kind. Sorry!", -1);

        private String description;
        private int httpCode;

        /**
         * Creates a new StatusCode with provided description and http code.
         *
         * @param description
         *          A message that describes the status or what might have caused it.
         * @param httpCode
         *          The Http response associated with this StatusCode.
         */
        StatusCode(String description, int httpCode) {
            this.description = description;
            this.httpCode = httpCode;
        }

        /**
         * Gets the StatusCode associated with this Http response code.
         *
         * @param code
         *          Http response code.
         * @return
         *          The StatusCode associated with the provided code.
         */
        public static StatusCode getStatus(int code) {
            switch (code) {
                case 1:
                    return UNKNOWN_HOST;
                case 200:
                    return SUCCESS;
                case 400:
                    return BAD_REQUEST;
                case 401:
                    return UNAUTHORIZED;
                case 403:
                    return FORBIDDEN;
                case 404:
                    return NOT_FOUND;
                case 413:
                    return FILE_TOO_BIG;
                case 429:
                    return UPLOAD_LIMITED;
                case 500:
                    return INTERNAL_SERVER_ERROR;
                case 502:
                    return SERVICE_UNAVAILABLE;
                default:
                    return UNKNOWN_ERROR;
            }
        }

        /**
         * Gets the string description of this StatusCode.
         *
         * @return
         *          The description of this StatusCode.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Gets the Http code associated with this StatusCode.
         *
         * @return
         *          The Http associated with this StatusCode.
         */
        public int getHttpCode() {
            return httpCode;
        }

        /**
         * Returns a string that represents this StatusCode.
         * Format: StatusCode - Name: [name] - HttpCode: [httpCode] - Description: [description]
         */
        @Override
        public String toString() {
            return String.format("StatusCode - %s: %s - %s: %d - %s: %s",
                    "Name", super.toString(),
                    "HttpCode", getHttpCode(),
                    "Description", getDescription());
        }
    }




}
