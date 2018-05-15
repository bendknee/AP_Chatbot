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
public class OriconComicRankingController {

	private static final Logger LOGGER = Logger.getLogger(OriconComicRankingController.class.getName());

	@EventMapping
	public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
		LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')", event.getTimestamp(),
				event.getMessage()));
		TextMessageContent content = event.getMessage();
		String contentText = content.getText();

		if (contentText.startsWith("/echo")) {
			return new TextMessage("echo dari oricon");
		} else if (contentText.startsWith("/oricon comic ")) {
			String replyText = contentText.replace("/oricon comic ", "");
			return new TextMessage(oriconResponse(replyText.substring(0)));
		} else if (contentText.startsWith("/")) {
			return new TextMessage("Command doesn't exist, try: \n" + "/oricon comic <date YYYY-MM-DD or YYYY-MM>");
		}
		return null;
	}

	@EventMapping
	public void handleDefaultMessage(Event event) {
		LOGGER.fine(String.format("Event(timestamp='%s',source='%s')", event.getTimestamp(), event.getSource()));
	}

	public static String oriconResponse(String date) throws Exception {
		String url = "https://www.oricon.co.jp/rank/cbm/";
		if (date.split("-").length == 2)
			url += "m/" + date + "/";
		else
			url += "w/" + date + "/";
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(url);

		HttpResponse response = client.execute(get);
		if (response.getStatusLine().getStatusCode() != 200)
			return "Invalid Parameter, either your date is not available or you have given a wrong input";
		Document html = Jsoup.connect(url).get();

		return scrapeComicRanking(html);

	}

	public static String scrapeComicRanking(Document html) {
		String result = "";
		Elements contents = html.select(".box-rank-entry");
		
		for (Element content : contents) {
            String chartPosition = content.getElementsByClass("num").text();
            String title = content.getElementsByClass("title").text();
            String author = content.getElementsByClass("artist-name").text();           
            result += "(" + chartPosition + ") " + title + " - " + author + "\n";
        }

		
		return result;
	}
}