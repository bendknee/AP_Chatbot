package advprog.example.bot.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@LineMessageHandler
public class SacredTextController {
	private static final Logger LOGGER = Logger.getLogger(SacredTextController.class.getName());
	private boolean hasChosed = false;
	private int chosenChapter = 0;

	@Autowired
	private LineMessagingClient lineMessagingClient;

	@EventMapping
	public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
		LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')", event.getTimestamp(),
				event.getMessage()));
		TextMessageContent content = event.getMessage();
		String contentText = content.getText();

		if (contentText.equals("/sacred_text") && !hasChosed) {

			ArrayList<CarouselColumn> carouselList = new ArrayList<CarouselColumn>();
			for (int i = 1; i <= 20; i++) {
				String img = "";
				carouselList.add(new CarouselColumn(img, "" + i, "The Rig Veda Book 1 HYMN " + i,
						Arrays.asList(new PostbackAction("Choose", "" + i))));
			}
			CarouselTemplate carouselTemplate = new CarouselTemplate(carouselList);
			TemplateMessage templateMessage = new TemplateMessage("Carousel alt text", carouselTemplate);
			reply(event.getReplyToken(), templateMessage);

		} else if (contentText.startsWith("/sacred_text ") && !hasChosed) {
			String[] replyTextArray = contentText.split(" ");

			// kalo input huruf? size gk sesuai?
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
			return new TextMessage(sacredResponse(chapter, verse));
		} else if (contentText.startsWith("/") && !hasChosed) {
			return new TextMessage("Command doesn't exist, try: kill yourself");
		} else if (hasChosed) {
			int verse = 0;
			try {
				verse = Integer.parseInt(contentText);
			} catch (Exception e) {
				return new TextMessage("Verse must be an integer, example : 1");
			}
			return new TextMessage(sacredResponse(chosenChapter, verse));

		}
		return new TextMessage("Previous process hasn't finished");
	}

	@EventMapping
	public void handleDefaultMessage(Event event) {
		LOGGER.fine(String.format("Event(timestamp='%s',source='%s')", event.getTimestamp(), event.getSource()));
	}

	public static String sacredResponse(int chapter, int verse) throws Exception {
		String url = String.format("http://www.sacred-texts.com/hin/rigveda/rv01%03d.htm", chapter);

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(url);

		HttpResponse response = client.execute(get);
		if (response.getStatusLine().getStatusCode() != 200)
			return "Chapter not available";
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
		for (String vers : verses) {
			result += vers.replace("<br>", "");
		}
		if (verse > verses.length || verse < 1) {
			result = "Invalid Verse Number\nVerse Range(inclusive): 1-" + verses.length;
		} else {
			result = verses[verse - 1].replace("<br> ", "\n");
			result = result.replaceAll("</(.*)>", "");
			result = result.replaceAll("<(.*)>", "");
		}

		return result;
	}

	@EventMapping
	public void handlePostbackEvent(PostbackEvent event) throws Exception {
		String replyToken = event.getReplyToken();
		String content = event.getPostbackContent().getData();
		chosenChapter = Integer.parseInt(content);
		this.reply(replyToken,
				Collections.singletonList(new TextMessage("Please choose a verse for Chapter " + chosenChapter)));
		hasChosed = true;
	}

	private void reply(String replyToken, Message message) {
		reply(replyToken, Collections.singletonList(message));
	}

	private void reply(String replyToken, List<Message> messages) {
		try {
			BotApiResponse apiResponse = lineMessagingClient.replyMessage(new ReplyMessage(replyToken, messages)).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

}
