package com.bot.botapakah;
import com.bot.Getter.Getter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
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

import java.util.Random;
import java.util.concurrent.ExecutionException;
import org.apache.tomcat.util.codec.binary.Base64;

@SpringBootApplication
@LineMessageHandler
public class BotApakahApplication extends SpringBootServletInitializer {

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

    public String nembakApi(String id) {
        String basic = getBasicAuth("acc_5923548cfcdb7aa", "b847a86333f34d3d417f456283b9462d");
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.line.me/v2/bot/message/"+id+"/content";

        UriComponentsBuilder uri = UriComponentsBuilder
                .fromUriString("https://api.imagga.com/v1/colors")
                .queryParam("url", url);

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", basic);

        HttpEntity<String> request = new HttpEntity<String>(String.valueOf(header));
        ResponseEntity<String> response = restTemplate.exchange(uri.toUriString(), HttpMethod.GET, request, String.class);

        return response.getBody();
        //jsonnode
    }

    static String getBasicAuth(String username, String password) {
        String auth = username + ":" + password;
        String auth64 = new String(Base64.encodeBase64(auth.getBytes()));

        return "Basic " + auth64;
    }

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


    private String jawabanmirrror(String pesan){
        String jawaban = pesan;
        StringBuilder jawaban1 = new StringBuilder();

        // append a string into StringBuilder jawaban1
        jawaban1.append(jawaban);

        // reverse StringBuilder jawaban1
        jawaban1 = jawaban1.reverse();
        return String.valueOf(jawaban1);
    }
    private String jawabanuntukilmukomputer(){
        String jawaban = "Samudera laut ilmu terhampar dihadapanku, cakrawala bersinar memanggilku kesana, kan ku seberangi lautan, tak ku kenal putus asa, dengan daya dan upaya untuk ilmu komputer.";
        return jawaban;
    }
    private String jawabanhalo(){
        String jawaban = "Halo Juga!";
        return jawaban;
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

}