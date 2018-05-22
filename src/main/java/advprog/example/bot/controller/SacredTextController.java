package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


@LineMessageHandler
public class SacredTextController {
	private static final Logger LOGGER = Logger.getLogger(SacredTextController.class.getName());

	@EventMapping
	public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
		LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')", event.getTimestamp(),
				event.getMessage()));
		TextMessageContent content = event.getMessage();
		String contentText = content.getText();

		if (contentText.equals("/sacred_text")) {
			String replyText = contentText.replace("/sacred_text", "");
			return null;
		}
		else if (contentText.startsWith("/sacred_text ")) {
			String[] replyTextArray = contentText.split(" ");
			
			
			// kalo input huruf? size gk sesuai?
			String[] value = replyTextArray[1].split(":");
			if(replyTextArray.length != 2 || value.length != 2){
				return new TextMessage("Parameter should just be <Chapter>:<Verse>");
			}
			int chapter = 0;
			int verse = 0;
			try{
				chapter = Integer.parseInt(value[0]);
				verse = Integer.parseInt(value[1]);
			}
			catch(Exception e){
				return new TextMessage("Chapter and Verse must both be an integer");
			}
			return new TextMessage(sacredResponse(chapter, verse));
		}
		else if (contentText.startsWith("/")) {
			return new TextMessage("Command doesn't exist, try: kill yourself");
		}
		return null;
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
		for(String vers : verses){
			result += vers.replace("<br>", "");
		}
		if(verse > verses.length || verse < 1){
			result = "Invalid Verse Number\nVerse Range(inclusive): 1-"
					+verses.length;
		}
		else{
			result = verses[verse-1].replace("<br> ", "\n");
			result = result.replaceAll("</(.*)>", "");
			result = result.replaceAll("<(.*)>", "");
		}		
		
		return result;
	}

}
