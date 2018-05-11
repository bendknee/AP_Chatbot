package advprog.example.bot.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.IOException;
import java.nio.file.Path;

@LineMessageHandler
public class HandWrittenExtract {
	private static final Logger LOGGER = Logger.getLogger(HandWrittenExtract.class.getName());

	@Autowired
	private LineMessagingClient lineMessagingClient;

	public static class DownloadedContent {
		Path path;
		String uri;
	}
	
	@EventMapping
	public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
		LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
				event.getTimestamp(), event.getMessage()));
		TextMessageContent content = event.getMessage();
		String contentText = content.getText();

		String replyText = contentText.replace("ocr this", "");
		return new TextMessage(replyText.substring(1));
	}

	@EventMapping
	public void handleImageMessageEvent(MessageEvent<ImageMessageContent> event) throws IOException {
		// You need to install ImageMagick
		handleHeavyContent(
				event.getReplyToken(),
				event.getMessage().getId(),
				responseBody -> {
					reply(event.getReplyToken(),
							new TextMessage("This is the binnary string" + responseBody));
				});
	}
	private void handleHeavyContent(String replyToken, String messageId, Consumer<MessageContentResponse> messageConsumer) {
		final MessageContentResponse response;
		try {
			response = lineMessagingClient.getMessageContent(messageId)
					.get();
		} catch (InterruptedException | ExecutionException e) {
			reply(replyToken, new TextMessage("Cannot get image: " + e.getMessage()));
			throw new RuntimeException(e);
		}
		messageConsumer.accept(response);
	}

	private void reply(String replyToken, Message message) {
		reply(replyToken, Collections.singletonList(message));
	}

	private void reply(String replyToken, List<Message> messages) {
		try {
			BotApiResponse apiResponse = lineMessagingClient
					.replyMessage(new ReplyMessage(replyToken, messages))
					.get();
			LOGGER.fine(String.format("Sent messages: {}", apiResponse));
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	@EventMapping
	public void handleDefaultMessage(Event event) {
		LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
				event.getTimestamp(), event.getSource()));
	}

}
