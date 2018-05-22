package advprog.example.bot.controller;

import advprog.example.bot.context.QuestionContext;
import advprog.example.bot.context.QuizContext;
import advprog.example.bot.entity.Question;
import advprog.example.bot.handler.EchoHandler;
import advprog.example.bot.handler.QuizHandler;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.event.source.UserSource;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;

@LineMessageHandler
public class BotController {

    private static final Logger LOGGER = Logger.getLogger(BotController.class.getName());
    private LineMessagingClient lineMessagingClient;
    private EchoHandler echoHandler;
    private QuizHandler quizHandler;
    private QuestionContext questionContext;
    private QuizContext quizContext;

    @Autowired
    public BotController(
            LineMessagingClient lineMessagingClient,
            EchoHandler echoHandler,
            QuizHandler quizHandler,
            QuestionContext questionContext,
            QuizContext quizContext
    ) {
        this.lineMessagingClient = lineMessagingClient;
        this.echoHandler = echoHandler;
        this.quizHandler = quizHandler;
        this.questionContext = questionContext;
        this.quizContext = quizContext;
    }

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));

        Source source = event.getSource();
        String senderId = source.getSenderId();
        String content = event.getMessage().getText();

        Message reply = null;

        if (content.matches("^stop zonk$")) {
            return quizHandler.handleStopZonk(senderId);
        } else if (content.matches("^next question$")) {
            return quizHandler.handleNextQuestion(senderId);
        } else if (content.matches("^start zonk$")) {
            if (source instanceof RoomSource || source instanceof GroupSource) {
                return quizHandler.handleStartZonk(senderId);
            } else {
                return new TextMessage("Room / Group chat only.");
            }
        }

        if (questionContext.containsAddContextKey(senderId)) {
            Question question = questionContext.getAddContext(senderId);

            if (!question.isQuestionSet()) {
                return quizHandler.handleSetQuestion(senderId, content);
            } else if (!question.isAnswersFilled()) {
                return quizHandler.handleAddAnswer(senderId, content);
            }
        }

        if (content.matches("^/echo .*")) {
            content = content.replace("/echo ", "");
            reply = echoHandler.composeReply(content);

        } else if (content.matches("^/add_question$")) {
            if (source instanceof UserSource) {
                reply = quizHandler.handleAddQuestion(senderId);
            } else {
                reply = new TextMessage("Private chat only.");
            }
        } else if (content.matches("^/change_answer$")) {
            if (source instanceof UserSource) {
                reply = quizHandler.handleChangeAnswer(senderId);
            } else {
                reply = new TextMessage("Private chat only.");
            }
        } else if (content.matches("^/bye$")) {
            if (source instanceof GroupSource) {
                lineMessagingClient.leaveGroup(((GroupSource) source).getGroupId()).get();
            } else if (source instanceof RoomSource) {
                lineMessagingClient.leaveRoom(((RoomSource) source).getRoomId()).get();
            }
        }

        return reply;
    }

    @EventMapping
    public Message handlePostbackEvent(PostbackEvent event) {
        String senderId = event.getSource().getSenderId();
        String data = event.getPostbackContent().getData();
        Message reply = null;

        if (questionContext.containsAddContextKey(senderId)) {
            Question question = questionContext.getAddContext(senderId);

            if (!question.hasCorrectAnswer()) {
                reply = quizHandler.handleSetCorrectAnswer(senderId, data);
            }
        } else if (questionContext.containsChangeContextKey(senderId)) {
            if (questionContext.getChangeContext(senderId) == null) {
                reply = quizHandler.handleMessageWithNullChangeContext(senderId, data);
            } else {
                reply = quizHandler.handleMessageWithChangeContext(senderId, data);
            }
        }

        return reply;
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}
