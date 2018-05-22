package advprog.example.bot.controller;

import advprog.example.bot.context.QuestionContext;
import advprog.example.bot.entity.QuestionEntity;
import advprog.example.bot.handler.EchoHandler;
import advprog.example.bot.handler.QuizHandler;
import advprog.example.bot.repository.AnswerRepository;
import advprog.example.bot.repository.QuestionRepository;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.action.PostbackAction;
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
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;

    @Autowired
    public BotController(
            LineMessagingClient lineMessagingClient,
            EchoHandler echoHandler,
            QuizHandler quizHandler,
            QuestionContext questionContext,
            QuestionRepository questionRepository,
            AnswerRepository answerRepository
    ) {
        this.lineMessagingClient = lineMessagingClient;
        this.echoHandler = echoHandler;
        this.quizHandler = quizHandler;
        this.questionContext = questionContext;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));

        Source source = event.getSource();
        String senderId = source.getSenderId();
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        Message reply = null;

        if (contentText.matches("^/echo .*")) {
            contentText = contentText.replace("/echo ", "");
            reply = echoHandler.composeReply(contentText);

        } else if (questionContext.containsAddContextKey(senderId)) {
            QuestionEntity question = questionContext.getAddContext(senderId);

            if (!question.isQuestionSet()) {
                reply = quizHandler.handleSetQuestion(senderId, contentText);
            } else if (!question.isAnswersFilled()) {
                reply = quizHandler.handleAddAnswer(senderId, contentText);
            }
        } else if (questionContext.containsChangeContextKey(senderId)) {
            if (questionContext.getChangeContext(senderId) == null) {
                reply = quizHandler.handleMessageWithNullChangeContext(senderId, contentText);
            } else {
                reply = quizHandler.handleMessageWithChangeContext(senderId, contentText);
            }
        } else if (contentText.matches("^/add_question$")) {
            if (source instanceof UserSource) {
                reply = quizHandler.handleAddQuestion(senderId);
            } else {
                reply = new TextMessage("Private chat only.");
            }
        } else if (contentText.matches("^/change_answer$")) {
            if (source instanceof UserSource) {
                reply = quizHandler.handleChangeAnswer(senderId);
            } else {
                reply = new TextMessage("Private chat only.");
            }
        } else if (contentText.matches("^/bye$")) {
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
            QuestionEntity question = questionContext.getAddContext(senderId);

            if (!question.hasCorrectAnswer()) {
                reply = quizHandler.handleSetCorrectAnswer(senderId, data);
            }
        } else if (questionContext.containsChangeContextKey(senderId)) {
            reply = quizHandler.handleMessageWithChangeContext(senderId, data);
        }

        return reply;
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}
