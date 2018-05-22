package advprog.example.bot.manager;

import advprog.example.bot.model.GuessAcronyms;
import advprog.example.bot.model.GuessAcronymsGroupState;
import advprog.example.bot.model.GuessAcronymsUserState;
import advprog.example.bot.repository.GuessAcronymsGroupStateRepository;
import advprog.example.bot.repository.GuessAcronymsRepository;
import advprog.example.bot.repository.GuessAcronymsUserStateRepository;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GuessAcronymsManager {

    private GuessAcronymsRepository guessAcronymsRepository;
    private GuessAcronymsUserStateRepository guessAcronymsUserStateRepository;
    private GuessAcronymsGroupStateRepository guessAcronymsGroupStateRepository;
    private LineMessagingClient lineMessagingClient;

    @Autowired
    public GuessAcronymsManager(GuessAcronymsRepository guessAcronymsRepository,
                                GuessAcronymsUserStateRepository guessAcronymsUserStateRepository,
                                GuessAcronymsGroupStateRepository guessAcronymsGroupStateRepository,
                                LineMessagingClient lineMessagingClient) {
        this.guessAcronymsRepository = guessAcronymsRepository;
        this.guessAcronymsUserStateRepository = guessAcronymsUserStateRepository;
        this.guessAcronymsGroupStateRepository = guessAcronymsGroupStateRepository;
        this.lineMessagingClient = lineMessagingClient;
    }

    private void reply(String replyToken, Message message) {
        try {
            lineMessagingClient.replyMessage(new ReplyMessage(replyToken, message)).get();
        } catch (Exception e) {
            // empty
        }
    }

    private Message addAcronymStartState(String userId) {
        GuessAcronymsUserState userState =
            guessAcronymsUserStateRepository.findByGroupIdAndUserId(userId, userId)
                                            .get();
        userState.setState("give acronym");
        guessAcronymsUserStateRepository.save(userState);
        return new TextMessage("Masukkan acronym");
    }

    private Message addAcronymGiveAcronymState(String userId, String acronym) {
        Optional<GuessAcronyms> guessAcronymsOptional =
            guessAcronymsRepository.findByAcronym(acronym);
        if (guessAcronymsOptional.isPresent()) {
            return new TextMessage("Acronym sudah ada");
        }

        GuessAcronymsUserState userState =
            guessAcronymsUserStateRepository.findByGroupIdAndUserId(userId, userId)
                                            .get();
        userState.setState("give expansion " + acronym);
        guessAcronymsUserStateRepository.save(userState);
        return new TextMessage("Masukkan expansion");
    }

    private Message addAcronymGiveExpansionState(String userId, String expansion) {
        GuessAcronymsUserState userState =
            guessAcronymsUserStateRepository.findByGroupIdAndUserId(userId, userId)
                                            .get();
        String acronym = userState.getState().substring("give expansion ".length());

        Optional<GuessAcronyms> guessAcronymsOptional =
            guessAcronymsRepository.findByAcronym(acronym);
        if (guessAcronymsOptional.isPresent()) {
            userState.setState("give acronym");
            guessAcronymsUserStateRepository.save(userState);
            return new TextMessage("Acronym sudah diambil orang lain, masukkan Acronym yang lain");
        }

        userState.setState("");
        guessAcronymsUserStateRepository.save(userState);
        guessAcronymsRepository.save(new GuessAcronyms(acronym, expansion));
        return new TextMessage(acronym + " (" + expansion + ")");
    }

    private Message updateAcronymStartState(String userId) {
        List<CarouselColumn> carouselColumns = new ArrayList<>();

        Iterable<GuessAcronyms> guessAcronymsIterable = guessAcronymsRepository.findAll();
        for (GuessAcronyms guessAcronyms : guessAcronymsIterable) {
            String acronym = guessAcronyms.getAcronym();
            String expansion = guessAcronyms.getExpansion();

            carouselColumns.add(new CarouselColumn("/static/buttons/1040.jpg",
                                                   acronym,
                                                   expansion,
                                                   Collections.singletonList(
                                                       new MessageAction("update acronym",
                                                                         "update acronym "
                                                                             + acronym))));
        }

        GuessAcronymsUserState userState =
            guessAcronymsUserStateRepository.findByGroupIdAndUserId(userId, userId)
                                            .get();
        userState.setState("update acronym");
        guessAcronymsUserStateRepository.save(userState);

        return new TemplateMessage("all acronym", new CarouselTemplate(carouselColumns));
    }

    private Message updateAcronymChooseAcronymState(String userId, String acronym) {
        Optional<GuessAcronyms> guessAcronymsOptional =
            guessAcronymsRepository.findByAcronym(acronym);

        if (guessAcronymsOptional.isPresent()) {
            GuessAcronymsUserState userState =
                guessAcronymsUserStateRepository.findByGroupIdAndUserId(userId, userId)
                                                .get();
            userState.setState("update expansion " + acronym);
            guessAcronymsUserStateRepository.save(userState);
            return new TextMessage("Masukkan expansion baru");
        }

        return updateAcronymStartState(userId);
    }

    private Message updateAcronymGiveExpansionState(String userId, String expansion) {
        GuessAcronymsUserState userState =
            guessAcronymsUserStateRepository.findByGroupIdAndUserId(userId, userId)
                                            .get();
        String acronym = userState.getState().substring("update expansion ".length());

        Optional<GuessAcronyms> guessAcronymsOptional =
            guessAcronymsRepository.findByAcronym(acronym);
        if (guessAcronymsOptional.isPresent()) {
            userState.setState("");
            guessAcronymsUserStateRepository.save(userState);

            GuessAcronyms guessAcronyms = guessAcronymsOptional.get();
            guessAcronyms.setExpansion(expansion);
            guessAcronymsRepository.save(guessAcronyms);

            return new TextMessage(acronym + " (" + expansion + ")");
        }

        return updateAcronymStartState(userId);
    }

    private Message deleteAcronymStartState(String userId) {
        List<CarouselColumn> carouselColumns = new ArrayList<>();

        Iterable<GuessAcronyms> guessAcronymsIterable = guessAcronymsRepository.findAll();
        for (GuessAcronyms guessAcronyms : guessAcronymsIterable) {
            String acronym = guessAcronyms.getAcronym();
            String expansion = guessAcronyms.getExpansion();

            carouselColumns.add(new CarouselColumn("/static/buttons/1040.jpg",
                                                   acronym,
                                                   expansion,
                                                   Collections.singletonList(
                                                       new MessageAction("delete acronym",
                                                                         "delete acronym "
                                                                             + acronym))));
        }

        GuessAcronymsUserState userState =
            guessAcronymsUserStateRepository.findByGroupIdAndUserId(userId, userId)
                                            .get();
        userState.setState("delete acronym");
        guessAcronymsUserStateRepository.save(userState);

        return new TemplateMessage("all acronym", new CarouselTemplate(carouselColumns));
    }

    private Message deleteAcronymChooseAcronymState(String userId, String acronym) {
        GuessAcronymsUserState userState =
            guessAcronymsUserStateRepository.findByGroupIdAndUserId(userId, userId)
                                            .get();
        userState.setState("confirm delete acronym " + acronym);
        guessAcronymsUserStateRepository.save(userState);
        return new TemplateMessage("confirm",
                                   new ConfirmTemplate("Yakin?",
                                                       new MessageAction("Ya", "Ya"),
                                                       new MessageAction("Tidak", "Tidak")));
    }

    private Message deleteAcronymConfirmationState(String userId, String message) {
        GuessAcronymsUserState userState =
            guessAcronymsUserStateRepository.findByGroupIdAndUserId(userId, userId)
                                            .get();
        String acronym = userState.getState().substring("confirm delete acronym ".length());
        userState.setState("");
        guessAcronymsUserStateRepository.save(userState);

        if (message.equals("Ya")) {
            Optional<GuessAcronyms> guessAcronymsOptional =
                guessAcronymsRepository.findByAcronym(acronym);

            if (guessAcronymsOptional.isPresent()) {
                guessAcronymsRepository.delete(guessAcronymsOptional.get());
            } else {
                return deleteAcronymStartState(userId);
            }
        }

        return null;
    }

    public void handlePrivateChat(String userId, String message, String replyToken) {
        GuessAcronymsUserState userState;
        Optional<GuessAcronymsUserState> userStateOptional =
            guessAcronymsUserStateRepository.findByGroupIdAndUserId(userId, userId);
        if (userStateOptional.isPresent()) {
            userState = userStateOptional.get();
        } else {
            userState = new GuessAcronymsUserState(userId, userId);
            guessAcronymsUserStateRepository.save(userState);
        }

        String state = userState.getState();
        Message replyMessage = null;
        if (state.equals("") && message.equals("/add_acronym")) {
            replyMessage = addAcronymStartState(userId);
        } else if (state.equals("give acronym")) {
            replyMessage = addAcronymGiveAcronymState(userId, message);
        } else if (state.startsWith("give expansion")) {
            replyMessage = addAcronymGiveExpansionState(userId, message);
        } else if (state.equals("") && message.equals("/update_acronym")) {
            replyMessage = updateAcronymStartState(userId);
        } else if (state.equals("update acronym")) {
            String acronym = message.substring("update acronym ".length());
            replyMessage = updateAcronymChooseAcronymState(userId, acronym);
        } else if (state.startsWith("update expansion")) {
            replyMessage = updateAcronymGiveExpansionState(userId, message);
        } else if (state.equals("") && message.equals("/delete_acronym")) {
            replyMessage = deleteAcronymStartState(userId);
        } else if (state.equals("delete acronym")) {
            String acronym = message.substring("delete acronym ".length());
            replyMessage = deleteAcronymChooseAcronymState(userId, acronym);
        } else if (state.startsWith("confirm delete acronym")) {
            replyMessage = deleteAcronymConfirmationState(userId, message);
        }

        if (replyMessage != null) {
            reply(replyToken, replyMessage);
        }
    }

    private boolean isActiveUser(String groupId, String userId) {
        try {
            if (groupId.startsWith("C")) {
                lineMessagingClient.getGroupMemberProfile(groupId, userId)
                                   .get()
                                   .getDisplayName();
            } else if (groupId.startsWith("R")) {
                lineMessagingClient.getRoomMemberProfile(groupId, userId)
                                   .get()
                                   .getDisplayName();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getDisplayName(String groupId, String userId) {
        try {
            if (groupId.startsWith("C")) {
                return lineMessagingClient.getGroupMemberProfile(groupId, userId)
                                          .get()
                                          .getDisplayName();
            } else if (groupId.startsWith("R")) {
                return lineMessagingClient.getRoomMemberProfile(groupId, userId)
                                          .get()
                                          .getDisplayName();
            }
        } catch (Exception e) {
            return "Error when get profile data";
        }
        return "Error when get profile data";
    }

    private Message playAcronymGameStart(String groupId) {
        Iterable<GuessAcronyms> guessAcronymsIterable =
            guessAcronymsRepository.findAll();
        List<GuessAcronyms> guessAcronyms = new ArrayList<>();
        guessAcronymsIterable.forEach(guessAcronyms::add);

        Random random = new Random();
        GuessAcronyms guessAcronym = guessAcronyms.get(random.nextInt(guessAcronyms.size()));
        String acronym = guessAcronym.getAcronym();

        GuessAcronymsGroupState groupState =
            guessAcronymsGroupStateRepository.findByGroupId(groupId)
                                             .get();
        groupState.setState("guess " + acronym);
        guessAcronymsGroupStateRepository.save(groupState);

        return new TextMessage(acronym);
    }

    private Message playAcronymGuessAttempt(String groupId, String userId, String guess) {
        GuessAcronymsGroupState groupState =
            guessAcronymsGroupStateRepository.findByGroupId(groupId)
                                             .get();
        String acronym = groupState.getState().substring("guess ".length());
        GuessAcronymsUserState userState =
            guessAcronymsUserStateRepository.findByGroupIdAndUserId(groupId, userId)
                                            .get();
        String guessAttemptString = userState.getState();
        if (guessAttemptString.equals("")) {
            guessAttemptString = "guess 0";
        }
        long guessAttempt = Long.parseLong(guessAttemptString.substring("guess ".length()));

        GuessAcronyms guessAcronyms =
            guessAcronymsRepository.findByAcronym(acronym)
                                   .get();
        String expansion = guessAcronyms.getExpansion();
        if (expansion.equals(guess) && guessAttempt < 3) {
            userState.setScore(userState.getScore() + 1);
            guessAcronymsUserStateRepository.save(userState);

            return playAcronymGameSkipAcronym(groupId);
        } else if (!expansion.equals(guess) && guessAttempt < 3) {
            userState.setState("guess " + (guessAttempt + 1));
            guessAcronymsUserStateRepository.save(userState);
        }

        return null;
    }

    private Message playAcronymGameSkipAcronym(String groupId) {
        List<GuessAcronymsUserState> userStates =
            guessAcronymsUserStateRepository.findByGroupId(groupId);
        userStates.forEach(userState -> userState.setState(""));
        return playAcronymGameStart(groupId);
    }

    private Message playAcronymGameStopGame(String groupId) {
        GuessAcronymsGroupState groupState =
            guessAcronymsGroupStateRepository.findByGroupId(groupId)
                                             .get();
        groupState.setState("");
        guessAcronymsGroupStateRepository.save(groupState);

        List<GuessAcronymsUserState> userStates =
            guessAcronymsUserStateRepository.findByGroupId(groupId);
        Collections.sort(userStates, Collections.reverseOrder());

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < userStates.size(); i++) {
            GuessAcronymsUserState userState = userStates.get(i);
            String userId = userState.getUserId();

            if (isActiveUser(groupId, userId)) {
                if (i != 0) {
                    stringBuilder.append("\n");
                }

                stringBuilder.append(i + 1);
                stringBuilder.append(". ");
                stringBuilder.append(getDisplayName(groupId, userId));
                stringBuilder.append(" - ");
                stringBuilder.append(userState.getScore());
            }
        }

        return new TextMessage(stringBuilder.toString());
    }

    public void handleGroupChat(String groupId, String userId, String message, String replyToken) {
        Optional<GuessAcronymsUserState> userStateOptional =
            guessAcronymsUserStateRepository.findByGroupIdAndUserId(groupId, userId);
        if (!userStateOptional.isPresent()) {
            guessAcronymsUserStateRepository.save(new GuessAcronymsUserState(groupId, userId));
        }

        GuessAcronymsGroupState groupState;
        Optional<GuessAcronymsGroupState> groupStateOptional =
            guessAcronymsGroupStateRepository.findByGroupId(groupId);
        if (groupStateOptional.isPresent()) {
            groupState = groupStateOptional.get();
        } else {
            groupState = new GuessAcronymsGroupState(groupId);
            guessAcronymsGroupStateRepository.save(groupState);
        }

        String state = groupState.getState();
        Message replyMessage = null;
        if (state.equals("") && message.contains("start acronym")) {
            replyMessage = playAcronymGameStart(groupId);
        } else if (state.startsWith("guess") && message.equals("next acronym")) {
            replyMessage = playAcronymGameSkipAcronym(groupId);
        } else if (state.startsWith("guess") && message.equals("stop acronym")) {
            replyMessage = playAcronymGameStopGame(groupId);
        } else if (state.startsWith("guess")) {
            replyMessage = playAcronymGuessAttempt(groupId, userId, message);
        }

        if (replyMessage != null) {
            reply(replyToken, replyMessage);
        }
    }
}
