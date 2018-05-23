package advprog.example.bot.handler;

import advprog.example.bot.context.QuestionContext;
import advprog.example.bot.context.QuizContext;
import advprog.example.bot.entity.Answer;
import advprog.example.bot.entity.Question;
import advprog.example.bot.entity.Quiz;
import advprog.example.bot.repository.AnswerRepository;
import advprog.example.bot.repository.QuestionRepository;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;

import com.linecorp.bot.model.profile.UserProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class QuizHandler {
    private LineMessagingClient lineMessagingClient;
    private QuestionContext questionContext;
    private QuizContext quizContext;
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;

    @Autowired
    public QuizHandler(
            LineMessagingClient lineMessagingClient,
            QuestionContext questionContext,
            QuizContext quizContext,
            QuestionRepository questionRepository,
            AnswerRepository answerRepository
    ) {
        this.lineMessagingClient = lineMessagingClient;
        this.questionContext = questionContext;
        this.quizContext = quizContext;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    public TextMessage handleAddQuestion(String userId) {
        List<Question> questions = questionRepository.findByCreatorId(userId);
        String reply;

        if (questions.size() <= 10) {
            questionContext.putAddContext(userId, new Question(userId));
            reply = "Enter your question.";
        } else {
            reply = "You can only make 10 questions.";
        }

        return new TextMessage(reply);
    }

    public TextMessage handleSetQuestion(String key, String message) {
        Question question = questionContext.getAddContext(key);
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
        Question question = questionContext.getAddContext(key);
        Message reply = null;

        if (message.length() <= 120) {
            question.addAnswer(new Answer(message, question));
        } else {
            reply = new TextMessage("Answer can't be more than 120 characters.");
        }

        if (question.isAnswersFilled()) {
            reply = createAnswersCarousel(question);
        }

        return reply;
    }

    public Message handleSetCorrectAnswer(String key, String message) {
        Question question = questionContext.getAddContext(key);
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

    private TemplateMessage createAnswersCarousel(Question question) {
        ArrayList<CarouselColumn> columns = new ArrayList<>();

        int index = 0;
        for (Answer answer: question.getAnswers()) {
            String id = Integer.toString(index++);
            columns.add(createCarouselColumn(answer.toString(), id));
        }

        return new TemplateMessage("Select correct answer", new CarouselTemplate(columns));
    }

    private TemplateMessage createQuestionsCarousel(String creatorId) {
        List<Question> questions = questionRepository.findByCreatorId(creatorId);
        ArrayList<CarouselColumn> columns = new ArrayList<>();

        for (Question question: questions) {
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
        Question question = questionContext.getChangeContext(key);

        question.setCorrectAnswerIndex(Integer.parseInt(message));
        questionContext.removeChangeContext(key);

        return new TextMessage(question.toString());
    }

    public Message handleMessageWithNullChangeContext(String key, String message) {
        Message reply = null;
        int id = Integer.parseInt(message);
        Optional<Question> question = questionRepository.findById(id);

        if (question.isPresent()) {
            questionContext.putChangeContext(key, question.get());
            reply = createAnswersCarousel(question.get());
        }

        return reply;
    }

    public TextMessage handleStartZonk(String senderId) {
        List<Question> questions = questionRepository.findAll();
        quizContext.putContext(senderId, new Quiz(questions));

        return handleNextQuestion(senderId);
    }

    public TextMessage handleNextQuestion(String senderId) {
        TextMessage reply = null;

        if (quizContext.containsContext(senderId)) {
            Quiz quiz = quizContext.getContext(senderId);
            Question question = quiz.getRandomQuestion();

            if (question != null) {
                String replyText = "Pertanyaan :\n" + question.getQuestion() + "\n\nJawaban :\n";

                for (Answer answer: question.getAnswers()) {
                    replyText += "- " + answer.toString() + "\n";
                }

                replyText += "Jawab langsung dengan jawabannya ya.";
                reply = new TextMessage(replyText);
                quiz.setCurrentQuestion(question);
            }
        }

        return reply;
    }

    public TextMessage handleStopZonk(String senderId) {
        TextMessage reply = null;

        if (quizContext.containsContext(senderId)) {
            reply = new TextMessage(composeEndMessage(senderId));
            quizContext.removeContext(senderId);
        }

        return reply;
    }

    private String composeEndMessage(String senderId) {
        String replyMessage = "";
        HashMap<String, Integer> scoreBoard = quizContext.getContext(senderId).getScores();

        Map<String, Integer> sortedScoreBoard =  scoreBoard
                .entrySet()
                .stream()
                .sorted(Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Entry::getKey,
                        Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        int juara = 1;
        for (Entry<String, Integer> entry: sortedScoreBoard.entrySet()) {
            String displayName = "(Hayoo belom ngeadd ya)";

            try {
                UserProfileResponse res = lineMessagingClient.getProfile(entry.getKey()).get();
                displayName = res.getDisplayName();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            replyMessage += (juara++) + ". " + displayName + " " + entry.getValue() + "\n";
        }

        return replyMessage + "\nGoodbye.";
    }

    public Message handleMessageOnActiveSession(String senderId, String userId, String content) {
        Quiz quiz = quizContext.getContext(senderId);
        TextMessage reply = null;

        Answer correctAnswer = quiz.getCurrentQuestion().getCorrectAnswer();
        if (correctAnswer.toString().equalsIgnoreCase(content)) {
            quiz.incrementUserScore(userId);
            reply = handleNextQuestion(senderId);
        }
        return reply;
    }
}
