package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
public class OriconBookRankingController {

	private static final Logger LOGGER = Logger.getLogger(OriconBookRankingController.class.getName());

	@EventMapping
	public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
		LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')", event.getTimestamp(),
				event.getMessage()));
		TextMessageContent content = event.getMessage();
		String contentText = content.getText();

		if (contentText.startsWith("/echo")) {
			return new TextMessage("echo dari oricon");
		} else if (contentText.startsWith("/oricon books weekly ")) {
			String replyText = contentText.replace("/oricon books weekly ", "");
			return new TextMessage(oriconResponse(replyText.substring(0)));
		} else if (contentText.startsWith("/")) {
			return new TextMessage("Command doesn't exist, try: \n" + "/oricon books weekly <date YYYY-MM-DD>");
		}
		return null;

	}

	@EventMapping
	public void handleDefaultMessage(Event event) {
		LOGGER.fine(String.format("Event(timestamp='%s',source='%s')", event.getTimestamp(), event.getSource()));
	}

	public static String oriconResponse(String date) throws Exception {
		String url = "https://www.oricon.co.jp/rank/ob/w/" + date + "/";		
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(url);

		HttpResponse response = client.execute(get);
		if (response.getStatusLine().getStatusCode() != 200)
			return "Invalid Parameter, either your date is not available or you have given a wrong input";
		Document html = Jsoup.connect(url).get();

		return scrapeComicRanking(html);

	}

	public static String scrapeBookRanking(Document html) {
		String result = "";
		Elements contents = html.select(".box-rank-entry");
		
		for (Element content : contents) {
            String chartPosition = content.getElementsByClass("num").text();
            String title = content.getElementsByClass("title").text();
            String author = content.getElementsByClass("name").text();           
            Elements list = content.getElementsByClass("list").get(0).getElementsByTag("li");
            String releaseMonth = list.get(1).text();
            releaseMonth = releaseMonth.substring(4, 11).replace("年", "-");
            String estimatedSales = list.get(3).text();
            estimatedSales = estimatedSales.substring(7).replace("部", "").replace(",", "");
            result += "(" + chartPosition + ") " + title + " - " + author + " - " + releaseMonth + " - " + estimatedSales + "\n";
        }

		
		return result;
	}

}
