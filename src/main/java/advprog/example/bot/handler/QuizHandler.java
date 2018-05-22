package advprog.example.bot.handler;

import advprog.example.bot.context.QuestionContext;
import advprog.example.bot.entity.AnswerEntity;
import advprog.example.bot.entity.QuestionEntity;
import advprog.example.bot.repository.AnswerRepository;
import advprog.example.bot.repository.QuestionRepository;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class QuizHandler {
    private QuestionContext questionContext;
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;

    @Autowired
    public QuizHandler(
            QuestionContext questionContext,
            QuestionRepository questionRepository,
            AnswerRepository answerRepository
    ) {
        this.questionContext = questionContext;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    public TextMessage handleAddQuestion(String userId) {
        List<QuestionEntity> questions = questionRepository.findByCreatorId(userId);
        String reply;

        if (questions.size() <= 10) {
            questionContext.putAddContext(userId, new QuestionEntity(userId));
            reply = "Enter your question.";
        } else {
            reply = "You can only make 10 questions.";
        }

        return new TextMessage(reply);
    }

    public TextMessage handleSetQuestion(String key, String message) {
        QuestionEntity question = questionContext.getAddContext(key);
        TextMessage reply;

        if (message.length() <= 120) {
            question.setQuestion(message);
            reply = new TextMessage("Enter 4 answers.");
        } else {
            reply = new TextMessage("Question can't be more than 120 characters.");
        }

        return reply;
    }

    public Message handleAddAnswer(String key, String message) {
        QuestionEntity question = questionContext.getAddContext(key);
        Message reply = null;

        if (message.length() <= 120) {
            question.addAnswer(new AnswerEntity(message, question));
        } else {
            reply = new TextMessage("Answer can't be more than 120 characters.");
        }

        if (question.isAnswersFilled()) {
            reply = createAnswersCarousel(question);
        }

        return reply;
    }

    public Message handleSetCorrectAnswer(String key, String message) {
        QuestionEntity question = questionContext.getAddContext(key);
        Message reply;

        try {
            int index = Integer.parseInt(message);

            if (index < 0 || index > 4) {
                throw new NumberFormatException();
            }

            question.setCorrectAnswerIndex(index);
            reply = new TextMessage(question.toString());

            questionRepository.save(question);
            answerRepository.saveAll(question.getAnswers());
            questionContext.removeAddContext(key);
        } catch (NumberFormatException e) {
            reply = createAnswersCarousel(question);
        }
        return reply;
    }

    private TemplateMessage createAnswersCarousel(QuestionEntity question) {
        ArrayList<CarouselColumn> columns = new ArrayList<>();

        int index = 0;
        for (AnswerEntity answer: question.getAnswers()) {
            String id = Integer.toString(index++);
            columns.add(createCarouselColumn(answer.toString(), id));
        }

        return new TemplateMessage("Select correct answer", new CarouselTemplate(columns));
    }

    private TemplateMessage createQuestionsCarousel(String creatorId) {
        List<QuestionEntity> questions = questionRepository.findByCreatorId(creatorId);
        ArrayList<CarouselColumn> columns = new ArrayList<>();

        for (QuestionEntity question: questions) {
            String id = Integer.toString(question.getId());
            columns.add(createCarouselColumn(question.getQuestion(), id));
        }

        return new TemplateMessage("Select question", new CarouselTemplate(columns));
    }

    private CarouselColumn createCarouselColumn(String text, String data) {
        return new CarouselColumn(
                null,
                null,
                text,
                Collections.singletonList(new PostbackAction("Pilih ini", data))
        );
    }

    public Message handleChangeAnswer(String userId) {
        Message reply;

        reply = createQuestionsCarousel(userId);
        questionContext.putChangeContext(userId, null);

        return reply;
    }

    public Message handleMessageWithChangeContext(String key, String message) {
        QuestionEntity questionEntity = questionContext.getChangeContext(key);
        questionEntity.setCorrectAnswerIndex(Integer.parseInt(message));
        questionContext.removeChangeContext(key);

        return new TextMessage(questionEntity.toString());
    }

    public Message handleMessageWithNullChangeContext(String key, String message) {
        Message reply = null;
        int id = Integer.parseInt(message);
        Optional<QuestionEntity> question = questionRepository.findById(id);

        if (question.isPresent()) {
            questionContext.putChangeContext(key, question.get());
            reply = createAnswersCarousel(question.get());
        }

        return reply;
    }

    public Message handleStartZonk() {
        return null;
    }

    public Message handleNextQuestion() {
        return null;
    }

    public Message handleStopZonk() {
        return null;
    }

    public Message handleMessageOnActiveSession() {
        return null;
    }
}
