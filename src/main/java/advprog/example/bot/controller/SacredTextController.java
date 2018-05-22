package advprog.example.bot.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;

@LineMessageHandler
public class SacredTextController {
    private static final Logger LOGGER = Logger.getLogger(SacredTextController.class.getName());
    private boolean hasChosed = false;
    private int chosenChapter = 0;
    private int randomChapter = 0;
    private int chancesRemaining = 0;

    @Autowired
    private LineMessagingClient lineMessagingClient;

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')", 
            event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        if (event.getSource() instanceof GroupSource) {
            if (contentText.equals("randomVerse")) {
                String output = "";
                if (chancesRemaining == 0) {
                    randomChapter = ThreadLocalRandom.current().nextInt(1, 192);
                    String randomVerse = randomVerse(randomChapter);
                    chancesRemaining = 5;
                    output = "Guess the chapter from Book 1 of The Rig Veda! \n"
                        + "Which chapter number that has the following verse:\n"
                        + randomVerse + "\nYou have 5 chances, good luck! ";
                } else {
                    output = "Another game is still ongoing\n"
                        + "Complete the game before starting a new one";
                }
                
                return new TextMessage(output);
            } else if (chancesRemaining > 0) {
                boolean isInteger = false;
                int userGuess = 0;
                try {
                    userGuess = Integer.parseInt(contentText.toString());
                    isInteger = true;
                } catch (Exception e) {
                    return null;
                }
                if (isInteger) {
                    String output = "";
                    if (userGuess == randomChapter) {
                        chancesRemaining = 0;
                        output = "You are correct";
                    } else {
                        output = "Incorrect\n";
                        if (--chancesRemaining > 0) {
                            output += "Try again, chances remaining: " + chancesRemaining;
                        } else {
                            output += "Correct answer is " + randomChapter;
                        }
                        
                    }
                    return new TextMessage(output);
                }
            } else if (contentText.equals("/bye")) {
                this.replyText(event.getReplyToken(), "Leaving group");
                lineMessagingClient.leaveGroup(
                    ((GroupSource) event.getSource()).getGroupId()).get();
            }

        } else {
            if (!hasChosed) {
                if (contentText.equals("/sacred_text")) {

                    /*
                     * ArrayList<CarouselColumn> carouselList = new
                     * ArrayList<CarouselColumn>(); for (int i = 1; i <= 20;
                     * i++) { String img = ""; carouselList.add(new
                     * CarouselColumn(img, "" + i, "The Rig Veda Book 1 HYMN " +
                     * i, Arrays.asList(new PostbackAction("Choose", "" + i))));
                     * } CarouselTemplate carouselTemplate = new
                     * CarouselTemplate(carouselList); TemplateMessage
                     * templateMessage = new
                     * TemplateMessage("Carousel alt text", carouselTemplate);
                     * System.out.println("check template: "
                     * +templateMessage+" #dah" ); return templateMessage;
                     */
                    ArrayList<CarouselColumn> carouselList = new ArrayList<CarouselColumn>();
                    for (int i = 1; i <= 20; i++) {
                        String img = "";
                        carouselList.add(new CarouselColumn(img, "" + i,
                            "The Rig Veda Book 1 HYMN " + i,
                            Arrays.asList(new PostbackAction("Choose", "" + i))));
                    }
                    CarouselTemplate carouselTemplate = new CarouselTemplate(carouselList);
                    TemplateMessage templateMessage = 
                        new TemplateMessage("Carousel alt text", carouselTemplate);
                    this.reply(event.getReplyToken(), templateMessage);

                } else if (contentText.startsWith("/sacred_text ")) {
                    String[] replyTextArray = contentText.split(" ");

                    String[] value = replyTextArray[1].split(":");
                    if (replyTextArray.length != 2 || value.length != 2) {
                        return new TextMessage("Parameter should just be <Chapter>:<Verse>");
                    }
                    int chapter = 0;
                    int verse = 0;
                    try {
                        chapter = Integer.parseInt(value[0]);
                        verse = Integer.parseInt(value[1]);
                    } catch (Exception e) {
                        return new TextMessage("Chapter and Verse must both be an integer");
                    }
                    hasChosed = false;
                    return new TextMessage(sacredResponse(chapter, verse));
                } else if (contentText.startsWith("/")) {
                    return new TextMessage("Command doesn't exist, try: kill yourself");
                }
            } else {
                if (contentText.startsWith("/")) {
                    return new TextMessage("Previous process hasn't finished");
                }
                int verse = 0;
                try {
                    verse = Integer.parseInt(contentText);
                } catch (Exception e) {
                    return new TextMessage("Verse must be an integer, example : 1");
                }
                return new TextMessage(sacredResponse(chosenChapter, verse));

            }
        }

        return null;
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
            event.getTimestamp(), event.getSource()));
    }

    public static String sacredResponse(int chapter, int verse) throws Exception {
        String url = String.format("http://www.sacred-texts.com/hin/rigveda/rv01%03d.htm", chapter);

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);

        HttpResponse response = client.execute(get);
        if (response.getStatusLine().getStatusCode() != 200) {
            return "Chapter not available";
        }
        Document html = Jsoup.connect(url).get();

        return scrapeVerse(html, verse);

    }

    public static String scrapeVerse(Document html, int verse) {
        String result = "";
        Element content = html.select("p:has(br)").first();
        String contentToStr = content.toString().replace("<p>", "").replaceAll("</p>", "");
        String regex = "<br>\\s\\d+(.)?\\s";
        String[] verses = contentToStr.split(regex);
        verses[0] = verses[0].replaceFirst("1(.)? ", "");
        /*for (String vers : verses) {
            result += vers.replace("<br>", "");
        }*/
        if (verse > verses.length || verse < 1) {
            result = "Invalid Verse Number\nVerse Range(inclusive): 1-" + verses.length;
        } else {
            result = verses[verse - 1].replace("<br> ", "\n");
            result = result.replaceAll("</(.*)>", "");
            result = result.replaceAll("<(.*)>", "");
        }

        return result;
    }

    public static String randomVerse(int chapter) throws Exception {
        String url = String.format("http://www.sacred-texts.com/hin/rigveda/rv01%03d.htm", chapter);
        
        Document html = Jsoup.connect(url).get();

        String result = "";
        Element content = html.select("p:has(br)").first();
        String contentToStr = content.toString().replace("<p>", "").replaceAll("</p>", "");
        String regex = "<br>\\s\\d+(.)?\\s";
        String[] verses = contentToStr.split(regex);
        int verse = ThreadLocalRandom.current().nextInt(0, verses.length);
        verses[0] = verses[0].replaceFirst("1(.)? ", "");

        result = verses[verse].replace("<br> ", "\n");
        result = result.replaceAll("</(.*)>", "");
        result = result.replaceAll("<(.*)>", "");

        return result;

    }

    @EventMapping
    public void handlePostbackEvent(PostbackEvent event) throws Exception {
        String replyToken = event.getReplyToken();
        String content = event.getPostbackContent().getData();
        chosenChapter = Integer.parseInt(content);
        this.reply(replyToken, Collections.singletonList(
            new TextMessage("Please choose a verse for Chapter " + chosenChapter)));
        hasChosed = true;
    }

    private void reply(String replyToken, Message message) {
        reply(replyToken, Collections.singletonList(message));
    }

    private void reply(String replyToken, List<Message> messages) {
        try {
            BotApiResponse apiResponse = lineMessagingClient.replyMessage(
                new ReplyMessage(replyToken, messages)).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void replyText(String replyToken, String message) {
        if (replyToken.isEmpty()) {
            throw new IllegalArgumentException("replyToken must not be empty");
        }
        if (message.length() > 1000) {
            message = message.substring(0, 1000 - 2) + "……";
        }
        this.reply(replyToken, new TextMessage(message));
    }

    @EventMapping
    public void handleJoinEvent(JoinEvent event) {
        String replyToken = event.getReplyToken();
        this.replyText(replyToken, "Joined " + event.getSource());
    }
}
