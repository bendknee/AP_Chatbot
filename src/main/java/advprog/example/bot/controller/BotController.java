package advprog.example.bot.controller;

import advprog.example.bot.BotExampleApplication;
import anime.bot.onair.AnimeOnAir;
import anime.bot.onair.SetUpAnime;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.*;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.jsoup.Connection;
import org.springframework.beans.factory.annotation.Autowired;


import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@LineMessageHandler
public class BotController {

    private static final Logger LOGGER = Logger.getLogger(BotController.class.getName());
    SetUpAnime setUpAnime = new SetUpAnime();

    @Autowired
    LineMessagingClient lineMessagingClient;


    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        String token = event.getReplyToken();
        String toReply= "";
        String year="";
        String season="";
        String genre="";
        if (contentText.contains("echo")) {
            String replyText = contentText.replace("/echo ", "");
            System.out.println(replyText.equalsIgnoreCase("lookup_anime"));
            if (replyText.equalsIgnoreCase("lookup_anime")) {
                carousel(token);
            }
        } else if (contentText.contains("/film")){
            String replyText = contentText.replace("/film", "");
            if (replyText.contains("/season")) {
                season = replyText.replace("/season ", "");
                setUpAnime.setSeason(season);
                toReply = season;
                carouselForYear(token);
            } else if (replyText.contains("/year")){
                year = replyText.replace("/year ", "");
                setUpAnime.setYear(year);
                setUpAnime.setUrl();

                AnimeOnAir animeOnAir = new AnimeOnAir();

                animeOnAir.getAnimeOnAir(setUpAnime.getUrl());

                String textMessage = animeOnAir.returnAnimeBasedOnGenreYearAndSeason(setUpAnime.getGenre());
                toReply = textMessage;
                Message textMessages = new TextMessage(textMessage);
                List<Message> messages = Arrays.asList(textMessages);
                lineMessagingClient.replyMessage(new ReplyMessage(token, messages));

            }else {
                genre = replyText.replace(" ", "");
                setUpAnime.setGenre(genre);
                toReply = genre;
                carouselForSeason(token);
            }
        } else {
            Message textMessage = new TextMessage(setUpAnime.getUrl());
            List<Message> messages = Arrays.asList(textMessage);
            lineMessagingClient.replyMessage(new ReplyMessage(token,messages));
        }

        return new TextMessage(toReply);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    private void reply(String token, TemplateMessage replies){
        try {
            lineMessagingClient.replyMessage(new ReplyMessage(token, replies)).get();

        } catch (InterruptedException |ExecutionException e) {
            System.out.println("Error");
        }
    }

    private void carousel(String replyToken){
        try {
            String imageUrl = URI.create("https://static/buttons/1040.jpg").toString();
            String imagesUrl = "data:text/plain;charset=utf-8;base64,L3N0YXRpYy9idXR0b25zLzEwNDAuanBn";
            CarouselTemplate carouselTemplate = new CarouselTemplate(
                    Arrays.asList(
                            new CarouselColumn("https://u.livechart.me/anime/poster_images/154/0957157a7117cf99523c2042cc46045f:small.jpg", "Genre", "Click One of The Genre", Arrays.asList(
                                    /*new URIAction("Go to line.me",
                                            "https://line.me"),
                                    new URIAction("Go to line.me",
                                            "https://line.me"),*/
                                    new PostbackAction("Action",
                                            "action", "/film Action"),
                                    new PostbackAction("Comedy",
                                            "comedy", "/film Comedy"),
                                    new PostbackAction("Sci-Fi",
                                            "sci-Fi", "/film Sci-Fi")

                            )),
                            new CarouselColumn("https://u.livechart.me/anime/poster_images/2526/bfcd73aac42a292cde2b996e4fcf082c.png:small.jpg", "Genre", "Click One of The Genre", Arrays.asList(
                                    new PostbackAction("Thiller",
                                            "thriller", "/film Thriller"),
                                    new PostbackAction("Romance",
                                            "romance", "/film Romance"),
                                    new PostbackAction("Adventure",
                                            "adventure", "/film Adventure")
                                    /*new PostbackAction("言 hello2",
                                            "hello こんにちは",
                                            "hello こんにちは"),
                                    new PostbackAction("言 hello2",
                                            "hello こんにちは",
                                            "hello こんにちは"),
                                    new MessageAction("Say message",
                                            "Rice=米")*/
                            )),
                            new CarouselColumn("https://u.livechart.me/anime/poster_images/2666/4bcb03db668aa7c1c1480cd5eebd45ab.png:small.jpg", "Genre", "Click One of The Genre", Arrays.asList(
                                    new PostbackAction("Horror",
                                            "horror", "/film Horror"),
                                    new PostbackAction("Drama",
                                            "drama", "/film Drama"),
                                    new PostbackAction("Supernatural",
                                            "supernatural", "/film Supernatural")
                                    /*new PostbackAction("言 hello2",
                                            "hello こんにちは",
                                            "hello こんにちは"),
                                    new PostbackAction("言 hello2",
                                            "hello こんにちは",
                                            "hello こんにちは"),
                                    new MessageAction("Say message",
                                            "Rice=米")*/
                            )),
                            new CarouselColumn("https://u.livechart.me/anime/poster_images/236/4164704792825607648280c87d916ea3:small.jpg", "Genre", "Click One of The Genre", Arrays.asList(
                                    new PostbackAction("Slice Of Life",
                                            "slice of life", "/film Slice of Life"),
                                    new PostbackAction("Fantasy",
                                            "fantasy", "/film Fantasy"),
                                    new PostbackAction("Historical",
                                            "historical", "/film Historical")
                                    /*new PostbackAction("言 hello2",
                                            "hello こんにちは",
                                            "hello こんにちは"),
                                    new PostbackAction("言 hello2",
                                            "hello こんにちは",
                                            "hello こんにちは"),
                                    new MessageAction("Say message",
                                            "Rice=米")*/
                            ))
                            /*new CarouselColumn(imageUrl, "Datetime Picker", "Please select a date, time or datetime", Arrays.asList(
                                    new DatetimePickerAction("Datetime",
                                            "action=sel",
                                            "datetime",
                                            "2017-06-18T06:15",
                                            "2100-12-31T23:59",
                                            "1900-01-01T00:00"),
                                    new DatetimePickerAction("Date",
                                            "action=sel&only=date",
                                            "date",
                                            "2017-06-18",
                                            "2100-12-31",
                                            "1900-01-01"),
                                    new DatetimePickerAction("Time",
                                            "action=sel&only=time",
                                            "time",
                                            "06:15",
                                            "23:59",
                                            "00:00")
                            ))*/
                    ));
            TemplateMessage templateMessage = new TemplateMessage("Carousel alt text", carouselTemplate);
            this.reply(replyToken, templateMessage);
        }
        catch (Exception e) {
            System.out.println("error");
        }

    }
    private void carouselForSeason(String replyToken){
        try {
            String imageUrl = "https://u.livechart.me/anime/poster_images/3011/ba6426ad69bea52f2e4dd1d419a2bd72:small.jpg";
            String imagesUrl = "data:text/plain;charset=utf-8;base64,L3N0YXRpYy9idXR0b25zLzEwNDAuanBn";
            CarouselTemplate carouselTemplate = new CarouselTemplate(
                    Arrays.asList(
                            new CarouselColumn(imageUrl, "Season", "Spring", Arrays.asList(
                                    new PostbackAction("Spring",
                                            "spring", "/film/season spring")
                            )),
                            new CarouselColumn(imageUrl, "Season", "Summer", Arrays.asList(
                                    new PostbackAction("Summer",
                                            "summer", "/film/season summer")
                            )),
                            new CarouselColumn(imageUrl, "Season", "Fall", Arrays.asList(
                                    new PostbackAction("Fall",
                                            "fall", "/film/season fall")
                            )),
                            new CarouselColumn(imageUrl, "Season", "Winter", Arrays.asList(
                                    new PostbackAction("Winter",
                                            "winter", "/film/season winter")
                            ))
                    ));
            TemplateMessage templateMessage = new TemplateMessage("Carousel alt text", carouselTemplate) ;
            this.reply(replyToken, templateMessage);
        }
        catch (Exception e) {
            System.out.println("error");
        }

    }

    private void carouselForYear(String replyToken){
        try {
            String imageUrl = "https://u.livechart.me/anime/poster_images/3011/ba6426ad69bea52f2e4dd1d419a2bd72:small.jpg";
            String imagesUrl = "data:text/plain;charset=utf-8;base64,L3N0YXRpYy9idXR0b25zLzEwNDAuanBn";
            CarouselTemplate carouselTemplate = new CarouselTemplate(
                    Arrays.asList(
                            new CarouselColumn(imageUrl, "Year", "Click One of The Year", Arrays.asList(
                                    new PostbackAction("2018",
                                            "2018", "/film/year 2018"),
                                    new PostbackAction("2017",
                                            "2017", "/film/year 2017"),
                                    new PostbackAction("2016",
                                            "2016", "/film/year 2016")
                            )),
                            new CarouselColumn(imageUrl, "Year", "Click One of The Year", Arrays.asList(
                                    new PostbackAction("2015",
                                            "2015", "/film/year 2015"),
                                    new PostbackAction("2014",
                                            "2014", "/film/year 2014"),
                                    new PostbackAction("2013",
                                            "2013", "/film/year 2013")

                            )),
                            new CarouselColumn(imageUrl, "Year", "Click One of The Year", Arrays.asList(
                                    new PostbackAction("2012",
                                            "2012", "/film/year 2012"),
                                    new PostbackAction("2011",
                                            "2011", "/film/year 2011"),
                                    new PostbackAction("2010",
                                            "2010", "/film/year 2010")
                            )),
                            new CarouselColumn(imageUrl, "Year", "Click One of The Year", Arrays.asList(
                                    new PostbackAction("2012",
                                            "2012", "/film/year 2012"),
                                    new PostbackAction("2011",
                                            "2011", "/film/year 2011"),
                                    new PostbackAction("2010",
                                            "2010", "/film/year 2010")
                            ))
                    ));
            TemplateMessage templateMessage = new TemplateMessage("Carousel alt text", carouselTemplate) ;
            this.reply(replyToken, templateMessage);
        }
        catch (Exception e) {
            System.out.println("error");
        }

    }

}
