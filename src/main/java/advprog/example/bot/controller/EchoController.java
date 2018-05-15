package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import static java.lang.String.format;

@LineMessageHandler
public class EchoController {

    private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        String replyText = contentText.replace("/echo", "");
        return new TextMessage(replyText.substring(1));
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    @EventMapping
    public void handleTextContentPublic (String replyToken, Event event, TextMessage content) throws Exception {
        handleTextContent(replyToken, event, content);
    }

    @EventMapping
    private ArrayList<String> scrapping(String mode, String date) throws IOException {
        String offset = "";
        ArrayList<String> val = new ArrayList<>();
        if (mode.toLowerCase().equals("daily")) {
            offset = "https://www.oricon.co.jp/rank/bd/d/"+date+"/";
        }
        else if (mode.toLowerCase().equals("weekly")) {
            offset = "https://www.oricon.co.jp/rank/bd/w/"+date+"/";
        }
        else {
            val.clear();
            val.add("Input Mode Salah!!!\n\n\nFormat input\n/oricon bluray <weekly|daily> YYYY-MM-DD");
            return val;
        }
        try {
            Document dc = Jsoup.connect(offset).timeout(3000).get();
            Elements body = dc.select("section.box-rank-entry");

            int i = 1;
            for (Element rank : body) {
                String title = rank.select("h2.title").text();
                String artist = rank.select("p.name").text();
                Elements list = rank.select("ul.list li");
                String release = list.get(0).text();
                release = release.substring(5, 9) + "-" + release.substring(10, 12) + "-" + release.substring(13, 15);
                val.add(format("(%d) %s - %s - %s", i, title, artist, release));
                i++;
            }
            return val;
        }
        catch (IOException e) {
            val.clear();
            val.add("Input tanggal salah atau tidak ditemukan!!!\n\n\nFormat input\n/oricon bluray <weekly|daily> YYYY-MM-DD");
            return val;
        }
    }

    @EventMapping
    private void handleTextContent(String replyToken, Event event, TextMessage content)
            throws Exception {
        String text = content.getText();

//        log.info("Got text message from {}: {}", replyToken, text);
//        switch (text) {
//            case "profile": {
//                String userId = event.getSource().getUserId();
//                if (userId != null) {
//                    lineMessagingClient
//                            .getProfile(userId)
//                            .whenComplete((profile, throwable) -> {
//                                if (throwable != null) {
//                                    this.replyText(replyToken, throwable.getMessage());
//                                    return;
//                                }
//
//                                this.reply(
//                                        replyToken,
//                                        Arrays.asList(new TextMessage(
//                                                        "Display name: " + profile.getDisplayName()),
//                                                new TextMessage("Status message: "
//                                                        + profile.getStatusMessage()))
//                                );
//
//                            });
//                } else {
//                    this.replyText(replyToken, "Bot can't use profile API without user ID");
//                }
//                break;
//            }
//            case "bye": {
//                Source source = event.getSource();
//                if (source instanceof GroupSource) {
//                    this.replyText(replyToken, "Leaving group");
//                    lineMessagingClient.leaveGroup(((GroupSource) source).getGroupId()).get();
//                } else if (source instanceof RoomSource) {
//                    this.replyText(replyToken, "Leaving room");
//                    lineMessagingClient.leaveRoom(((RoomSource) source).getRoomId()).get();
//                } else {
//                    this.replyText(replyToken, "Bot can't leave from 1:1 chat");
//                }
//                break;
//            }
//            case "confirm": {
//                ConfirmTemplate confirmTemplate = new ConfirmTemplate(
//                        "Do it?",
//                        new MessageAction("Yes", "Yes!"),
//                        new MessageAction("No", "No!")
//                );
//                TemplateMessage templateMessage = new TemplateMessage("Confirm alt text", confirmTemplate);
//                this.reply(replyToken, templateMessage);
//                break;
//            }
//            case "buttons": {
//                String imageUrl = createUri("/static/buttons/1040.jpg");
//                ButtonsTemplate buttonsTemplate = new ButtonsTemplate(
//                        imageUrl,
//                        "My button sample",
//                        "Hello, my button",
//                        Arrays.asList(
//                                new URIAction("Go to line.me",
//                                        "https://line.me"),
//                                new PostbackAction("Say hello1",
//                                        "hello こんにちは"),
//                                new PostbackAction("言 hello2",
//                                        "hello こんにちは",
//                                        "hello こんにちは"),
//                                new MessageAction("Say message",
//                                        "Rice=米")
//                        ));
//                TemplateMessage templateMessage = new TemplateMessage("Button alt text", buttonsTemplate);
//                this.reply(replyToken, templateMessage);
//                break;
//            }
//            case "carousel": {
//                String imageUrl = createUri("/static/buttons/1040.jpg");
//                CarouselTemplate carouselTemplate = new CarouselTemplate(
//                        Arrays.asList(
//                                new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
//                                        new URIAction("Go to line.me",
//                                                "https://line.me"),
//                                        new URIAction("Go to line.me",
//                                                "https://line.me"),
//                                        new PostbackAction("Say hello1",
//                                                "hello こんにちは")
//                                )),
//                                new CarouselColumn(imageUrl, "hoge", "fuga", Arrays.asList(
//                                        new PostbackAction("言 hello2",
//                                                "hello こんにちは",
//                                                "hello こんにちは"),
//                                        new PostbackAction("言 hello2",
//                                                "hello こんにちは",
//                                                "hello こんにちは"),
//                                        new MessageAction("Say message",
//                                                "Rice=米")
//                                )),
//                                new CarouselColumn(imageUrl, "Datetime Picker", "Please select a date, time or datetime", Arrays.asList(
//                                        new DatetimePickerAction("Datetime",
//                                                "action=sel",
//                                                "datetime",
//                                                "2017-06-18T06:15",
//                                                "2100-12-31T23:59",
//                                                "1900-01-01T00:00"),
//                                        new DatetimePickerAction("Date",
//                                                "action=sel&only=date",
//                                                "date",
//                                                "2017-06-18",
//                                                "2100-12-31",
//                                                "1900-01-01"),
//                                        new DatetimePickerAction("Time",
//                                                "action=sel&only=time",
//                                                "time",
//                                                "06:15",
//                                                "23:59",
//                                                "00:00")
//                                ))
//                        ));
//                TemplateMessage templateMessage = new TemplateMessage("Carousel alt text", carouselTemplate);
//                this.reply(replyToken, templateMessage);
//                break;
//            }
//            case "image_carousel": {
//                String imageUrl = createUri("/static/buttons/1040.jpg");
//                ImageCarouselTemplate imageCarouselTemplate = new ImageCarouselTemplate(
//                        Arrays.asList(
//                                new ImageCarouselColumn(imageUrl,
//                                        new URIAction("Goto line.me",
//                                                "https://line.me")
//                                ),
//                                new ImageCarouselColumn(imageUrl,
//                                        new MessageAction("Say message",
//                                                "Rice=米")
//                                ),
//                                new ImageCarouselColumn(imageUrl,
//                                        new PostbackAction("言 hello2",
//                                                "hello こんにちは",
//                                                "hello こんにちは")
//                                )
//                        ));
//                TemplateMessage templateMessage = new TemplateMessage("ImageCarousel alt text", imageCarouselTemplate);
//                this.reply(replyToken, templateMessage);
//                break;
//            }
//            case "imagemap":
//                this.reply(replyToken, new ImagemapMessage(
//                        createUri("/static/rich"),
//                        "This is alt text",
//                        new ImagemapBaseSize(1040, 1040),
//                        Arrays.asList(
//                                new URIImagemapAction(
//                                        "https://store.line.me/family/manga/en",
//                                        new ImagemapArea(
//                                                0, 0, 520, 520
//                                        )
//                                ),
//                                new URIImagemapAction(
//                                        "https://store.line.me/family/music/en",
//                                        new ImagemapArea(
//                                                520, 0, 520, 520
//                                        )
//                                ),
//                                new URIImagemapAction(
//                                        "https://store.line.me/family/play/en",
//                                        new ImagemapArea(
//                                                0, 520, 520, 520
//                                        )
//                                ),
//                                new MessageImagemapAction(
//                                        "URANAI!",
//                                        new ImagemapArea(
//                                                520, 520, 520, 520
//                                        )
//                                )
//                        )
//                ));
//                break;
//            default:
//                log.info("Returns echo message {}: {}", replyToken, text);
//                this.replyText(
//                        replyToken,
//                        text
//                );
//                break;
//        }
    }
}
