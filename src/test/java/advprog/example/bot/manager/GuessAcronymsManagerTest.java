package advprog.example.bot.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import advprog.example.bot.model.GuessAcronyms;
import advprog.example.bot.model.GuessAcronymsGroupState;
import advprog.example.bot.model.GuessAcronymsUserState;
import advprog.example.bot.repository.GuessAcronymsGroupStateRepository;
import advprog.example.bot.repository.GuessAcronymsRepository;
import advprog.example.bot.repository.GuessAcronymsUserStateRepository;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.profile.UserProfileResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class GuessAcronymsManagerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    private GuessAcronymsManager guessAcronymsManager;

    @Mock
    private GuessAcronymsRepository guessAcronymsRepository;
    @Mock
    private GuessAcronymsUserStateRepository guessAcronymsUserStateRepository;
    @Mock
    private GuessAcronymsGroupStateRepository guessAcronymsGroupStateRepository;
    @Mock
    private LineMessagingClient lineMessagingClient;

    @BeforeEach
    void setUp() {
        guessAcronymsManager = new GuessAcronymsManager(guessAcronymsRepository,
                                                        guessAcronymsUserStateRepository,
                                                        guessAcronymsGroupStateRepository,
                                                        lineMessagingClient);
        doReturn(CompletableFuture
                     .completedFuture(new UserProfileResponse("Endrawan",
                                                              "U1",
                                                              "", "")))
            .when(lineMessagingClient)
            .getGroupMemberProfile("C1", "U1");
        doReturn(CompletableFuture
                     .completedFuture(new UserProfileResponse("Endrawan",
                                                              "U1",
                                                              "", "")))
            .when(lineMessagingClient)
            .getRoomMemberProfile("R1", "U1");
    }

    @Test
    void testPrivateChatNoReply() {
        GuessAcronymsUserState guessAcronymsUserState =
            new GuessAcronymsUserState("U1", "U1", "", 0);
        doReturn(Optional.of(guessAcronymsUserState))
            .when(guessAcronymsUserStateRepository)
            .findByGroupIdAndUserId("U1", "U1");
        guessAcronymsManager.handlePrivateChat("U1", "Hallo", "replyToken");
        verify(lineMessagingClient, never()).replyMessage(any());
    }

    @Test
    void testAddAcronymStartState() {
        GuessAcronymsUserState guessAcronymsUserState =
            new GuessAcronymsUserState("U1", "U1", "", 0);
        doReturn(Optional.of(guessAcronymsUserState))
            .when(guessAcronymsUserStateRepository)
            .findByGroupIdAndUserId("U1", "U1");
        guessAcronymsManager.handlePrivateChat("U1", "/add_acronym", "replyToken");
        verify(lineMessagingClient, atLeastOnce())
            .replyMessage(new ReplyMessage("replyToken", new TextMessage("Masukkan acronym")));
        assertEquals("give acronym", guessAcronymsUserState.getState());
    }

    @Test
    void testAddAcronymGiveAcronymState() {
        GuessAcronymsUserState guessAcronymsUserState =
            new GuessAcronymsUserState("U1", "U1", "give acronym", 0);
        doReturn(Optional.of(guessAcronymsUserState))
            .when(guessAcronymsUserStateRepository)
            .findByGroupIdAndUserId("U1", "U1");
        doReturn(Optional.empty())
            .when(guessAcronymsRepository)
            .findByAcronym("EAW");
        guessAcronymsManager.handlePrivateChat("U1", "EAW", "replyToken");
        verify(lineMessagingClient, atLeastOnce())
            .replyMessage(new ReplyMessage("replyToken", new TextMessage("Masukkan expansion")));
        assertEquals("give expansion EAW", guessAcronymsUserState.getState());
    }

    @Test
    void testAddAcronymGiveAcronymStateAlreadyExist() {
        GuessAcronymsUserState guessAcronymsUserState =
            new GuessAcronymsUserState("U1", "U1", "give acronym", 0);
        doReturn(Optional.of(guessAcronymsUserState))
            .when(guessAcronymsUserStateRepository)
            .findByGroupIdAndUserId("U1", "U1");
        doReturn(Optional.of(new GuessAcronyms("EAW", "Endrawan Andika Wicaksana")))
            .when(guessAcronymsRepository)
            .findByAcronym("EAW");
        guessAcronymsManager.handlePrivateChat("U1", "EAW", "replyToken");
        verify(lineMessagingClient, atLeastOnce())
            .replyMessage(new ReplyMessage("replyToken", new TextMessage("Acronym sudah ada")));
        assertEquals("give acronym", guessAcronymsUserState.getState());
    }

    @Test
    void testAddAcronymGiveExpansionState() {
        GuessAcronymsUserState guessAcronymsUserState =
            new GuessAcronymsUserState("U1", "U1", "give expansion EAW", 0);
        doReturn(Optional.of(guessAcronymsUserState))
            .when(guessAcronymsUserStateRepository)
            .findByGroupIdAndUserId("U1", "U1");
        doReturn(Optional.empty())
            .when(guessAcronymsRepository)
            .findByAcronym("EAW");
        guessAcronymsManager.handlePrivateChat("U1", "Endrawan Andika Wicaksana", "replyToken");
        verify(lineMessagingClient, atLeastOnce())
            .replyMessage(new ReplyMessage("replyToken",
                                           new TextMessage("EAW (Endrawan Andika Wicaksana)")));
        assertEquals("", guessAcronymsUserState.getState());
    }

    @Test
    void testAddAcronymGiveExpansionStateAlreadyExist() {
        GuessAcronymsUserState guessAcronymsUserState =
            new GuessAcronymsUserState("U1", "U1", "give expansion EAW", 0);
        doReturn(Optional.of(guessAcronymsUserState))
            .when(guessAcronymsUserStateRepository)
            .findByGroupIdAndUserId("U1", "U1");
        doReturn(Optional.of(new GuessAcronyms("EAW", "Endrawan Andika Wicaksana")))
            .when(guessAcronymsRepository)
            .findByAcronym("EAW");
        guessAcronymsManager.handlePrivateChat("U1", "Endrawan Andika Wicaksana", "replyToken");
        verify(lineMessagingClient, atLeastOnce())
            .replyMessage(new ReplyMessage("replyToken",
                                           new TextMessage("Acronym sudah diambil orang lain, "
                                                               + "masukkan Acronym yang lain")));
        assertEquals("give acronym", guessAcronymsUserState.getState());
    }

    @Test
    void testUpdateAcronymStartState() {
        GuessAcronymsUserState guessAcronymsUserState =
            new GuessAcronymsUserState("U1", "U1", "", 0);
        doReturn(Optional.of(guessAcronymsUserState))
            .when(guessAcronymsUserStateRepository)
            .findByGroupIdAndUserId("U1", "U1");
        List<GuessAcronyms> guessAcronyms = new ArrayList<>();
        guessAcronyms.add(new GuessAcronyms("EAW", "Endrawan Andika Wicaksana"));
        doReturn(guessAcronyms)
            .when(guessAcronymsRepository)
            .findAll();
        guessAcronymsManager.handlePrivateChat("U1", "/update_acronym", "replyToken");
        List<CarouselColumn> carouselColumns = new ArrayList<>();
        carouselColumns.add(new CarouselColumn("/static/buttons/1040.jpg",
                                               "EAW",
                                               "Endrawan Andika Wicaksana",
                                               Collections.singletonList(
                                                   new MessageAction("update acronym",
                                                                     "update acronym EAW"))));
        verify(lineMessagingClient, atLeastOnce())
            .replyMessage(new ReplyMessage("replyToken",
                                           new TemplateMessage("all acronym",
                                                               new CarouselTemplate(
                                                                   carouselColumns))));
        assertEquals("update acronym", guessAcronymsUserState.getState());
    }

    @Test
    void testUpdateAcronymChooseAcronymState() {
        GuessAcronymsUserState guessAcronymsUserState =
            new GuessAcronymsUserState("U1", "U1", "update acronym", 0);
        doReturn(Optional.of(guessAcronymsUserState))
            .when(guessAcronymsUserStateRepository)
            .findByGroupIdAndUserId("U1", "U1");
        doReturn(Optional.of(new GuessAcronyms("EAW", "Endrawan Andika Wicaksana")))
            .when(guessAcronymsRepository)
            .findByAcronym("EAW");
        guessAcronymsManager.handlePrivateChat("U1", "update acronym EAW", "replyToken");
        verify(lineMessagingClient, atLeastOnce())
            .replyMessage(new ReplyMessage("replyToken",
                                           new TextMessage("Masukkan expansion baru")));
        assertEquals("update expansion EAW", guessAcronymsUserState.getState());
    }

    @Test
    void testUpdateAcronymGiveExpansionState() {
        GuessAcronymsUserState guessAcronymsUserState =
            new GuessAcronymsUserState("U1", "U1", "update expansion EAW", 0);
        doReturn(Optional.of(guessAcronymsUserState))
            .when(guessAcronymsUserStateRepository)
            .findByGroupIdAndUserId("U1", "U1");
        doReturn(Optional.of(new GuessAcronyms("EAW", "Endrawan Andika Wicaksana")))
            .when(guessAcronymsRepository)
            .findByAcronym("EAW");
        guessAcronymsManager.handlePrivateChat("U1", "Endrawan Andika W", "replyToken");
        verify(lineMessagingClient, atLeastOnce())
            .replyMessage(new ReplyMessage("replyToken",
                                           new TextMessage("EAW (Endrawan Andika W)")));
        assertEquals("", guessAcronymsUserState.getState());
    }

    @Test
    void testDeleteAcronymStartState() {
        GuessAcronymsUserState guessAcronymsUserState =
            new GuessAcronymsUserState("U1", "U1", "", 0);
        doReturn(Optional.of(guessAcronymsUserState))
            .when(guessAcronymsUserStateRepository)
            .findByGroupIdAndUserId("U1", "U1");
        List<GuessAcronyms> guessAcronyms = new ArrayList<>();
        guessAcronyms.add(new GuessAcronyms("EAW", "Endrawan Andika Wicaksana"));
        doReturn(guessAcronyms)
            .when(guessAcronymsRepository)
            .findAll();
        guessAcronymsManager.handlePrivateChat("U1", "/delete_acronym", "replyToken");
        List<CarouselColumn> carouselColumns = new ArrayList<>();
        carouselColumns.add(new CarouselColumn("/static/buttons/1040.jpg",
                                               "EAW",
                                               "Endrawan Andika Wicaksana",
                                               Collections.singletonList(
                                                   new MessageAction("delete acronym",
                                                                     "delete acronym EAW"))));
        verify(lineMessagingClient, atLeastOnce())
            .replyMessage(new ReplyMessage("replyToken",
                                           new TemplateMessage("all acronym",
                                                               new CarouselTemplate(
                                                                   carouselColumns))));
        assertEquals("delete acronym", guessAcronymsUserState.getState());
    }

    @Test
    void testDeleteAcronymChooseAcronymState() {
        GuessAcronymsUserState guessAcronymsUserState =
            new GuessAcronymsUserState("U1", "U1", "delete acronym", 0);
        doReturn(Optional.of(guessAcronymsUserState))
            .when(guessAcronymsUserStateRepository)
            .findByGroupIdAndUserId("U1", "U1");
        guessAcronymsManager.handlePrivateChat("U1", "delete acronym EAW", "replyToken");
        verify(lineMessagingClient, atLeastOnce())
            .replyMessage(new ReplyMessage("replyToken",
                                           new TemplateMessage("confirm",
                                                               new ConfirmTemplate(
                                                                   "Yakin?",
                                                                   new MessageAction(
                                                                       "Ya",
                                                                       "Ya"),
                                                                   new MessageAction(
                                                                       "Tidak",
                                                                       "Tidak")))));
        assertEquals("confirm delete acronym EAW", guessAcronymsUserState.getState());
    }

    @Test
    void testDeleteAcronymConfirmationStateYes() {
        GuessAcronymsUserState guessAcronymsUserState =
            new GuessAcronymsUserState("U1", "U1", "confirm delete acronym EAW", 0);
        doReturn(Optional.of(guessAcronymsUserState))
            .when(guessAcronymsUserStateRepository)
            .findByGroupIdAndUserId("U1", "U1");
        GuessAcronyms guessAcronyms = new GuessAcronyms("EAW", "Endrawan Andika Wicaksana");
        doReturn(Optional.of(guessAcronyms))
            .when(guessAcronymsRepository)
            .findByAcronym("EAW");
        guessAcronymsManager.handlePrivateChat("U1", "Ya", "replyToken");
        verify(guessAcronymsRepository, atLeastOnce()).delete(guessAcronyms);
        assertEquals("", guessAcronymsUserState.getState());
    }

    @Test
    void testDeleteAcronymConfirmationStateNo() {
        GuessAcronymsUserState guessAcronymsUserState =
            new GuessAcronymsUserState("U1", "U1", "confirm delete acronym EAW", 0);
        doReturn(Optional.of(guessAcronymsUserState))
            .when(guessAcronymsUserStateRepository)
            .findByGroupIdAndUserId("U1", "U1");
        GuessAcronyms guessAcronyms = new GuessAcronyms("EAW", "Endrawan Andika Wicaksana");
        doReturn(Optional.of(guessAcronyms))
            .when(guessAcronymsRepository)
            .findByAcronym("EAW");
        guessAcronymsManager.handlePrivateChat("U1", "Tidak", "replyToken");
        verify(guessAcronymsRepository, never()).delete(guessAcronyms);
        assertEquals("", guessAcronymsUserState.getState());
    }

    @Test
    void testGroupChatNoReply() {
        GuessAcronymsUserState guessAcronymsUserState =
            new GuessAcronymsUserState("C1", "U1", "", 0);
        doReturn(Optional.of(guessAcronymsUserState))
            .when(guessAcronymsUserStateRepository)
            .findByGroupIdAndUserId("C1", "U1");
        GuessAcronymsGroupState guessAcronymsGroupState =
            new GuessAcronymsGroupState("C1", "");
        doReturn(Optional.of(guessAcronymsGroupState))
            .when(guessAcronymsGroupStateRepository)
            .findByGroupId("C1");
        guessAcronymsManager.handleGroupChat("C1", "U1", "Hallo", "replyToken");
        verify(lineMessagingClient, never()).replyMessage(any());
    }

    @Test
    void testPlayAcronymGameStart() {
        GuessAcronymsUserState guessAcronymsUserState =
            new GuessAcronymsUserState("C1", "U1", "", 0);
        doReturn(Optional.of(guessAcronymsUserState))
            .when(guessAcronymsUserStateRepository)
            .findByGroupIdAndUserId("C1", "U1");
        GuessAcronymsGroupState guessAcronymsGroupState =
            new GuessAcronymsGroupState("C1", "");
        doReturn(Optional.of(guessAcronymsGroupState))
            .when(guessAcronymsGroupStateRepository)
            .findByGroupId("C1");
        List<GuessAcronyms> guessAcronyms = new ArrayList<>();
        GuessAcronyms guessAcronym = new GuessAcronyms("EAW", "Endrawan Andika Wicaksana");
        guessAcronyms.add(guessAcronym);
        doReturn(guessAcronyms)
            .when(guessAcronymsRepository)
            .findAll();
        guessAcronymsManager.handleGroupChat("C1", "U1", "start acronym", "replyToken");
        verify(lineMessagingClient, atLeastOnce())
            .replyMessage(new ReplyMessage("replyToken", new TextMessage("EAW")));
        assertEquals("guess EAW", guessAcronymsGroupState.getState());
    }

    @Test
    void testPlayAcronymGuessAttemptTrue() {
        GuessAcronymsUserState guessAcronymsUserState =
            new GuessAcronymsUserState("C1", "U1", "", 0);
        doReturn(Optional.of(guessAcronymsUserState))
            .when(guessAcronymsUserStateRepository)
            .findByGroupIdAndUserId("C1", "U1");
        GuessAcronymsGroupState guessAcronymsGroupState =
            new GuessAcronymsGroupState("C1", "guess EAW");
        doReturn(Optional.of(guessAcronymsGroupState))
            .when(guessAcronymsGroupStateRepository)
            .findByGroupId("C1");
        GuessAcronyms guessAcronym = new GuessAcronyms("EAW", "Endrawan Andika Wicaksana");
        doReturn(Optional.of(guessAcronym))
            .when(guessAcronymsRepository)
            .findByAcronym("EAW");
        List<GuessAcronyms> guessAcronyms = new ArrayList<>();
        GuessAcronyms guessAcronym2 = new GuessAcronyms("EA", "Endrawan Andika");
        guessAcronyms.add(guessAcronym2);
        doReturn(guessAcronyms)
            .when(guessAcronymsRepository)
            .findAll();
        guessAcronymsManager.handleGroupChat("C1", "U1", "Endrawan Andika Wicaksana", "replyToken");
        verify(lineMessagingClient, atLeastOnce())
            .replyMessage(new ReplyMessage("replyToken", new TextMessage("EA")));
        assertEquals("guess EA", guessAcronymsGroupState.getState());
        assertEquals(1, guessAcronymsUserState.getScore());
    }

    @Test
    void testPlayAcronymGuessAttemptFalse() {
        GuessAcronymsUserState guessAcronymsUserState =
            new GuessAcronymsUserState("C1", "U1", "", 0);
        doReturn(Optional.of(guessAcronymsUserState))
            .when(guessAcronymsUserStateRepository)
            .findByGroupIdAndUserId("C1", "U1");
        GuessAcronymsGroupState guessAcronymsGroupState =
            new GuessAcronymsGroupState("C1", "guess EAW");
        doReturn(Optional.of(guessAcronymsGroupState))
            .when(guessAcronymsGroupStateRepository)
            .findByGroupId("C1");
        GuessAcronyms guessAcronym = new GuessAcronyms("EAW", "Endrawan Andika Wicaksana");
        doReturn(Optional.of(guessAcronym))
            .when(guessAcronymsRepository)
            .findByAcronym("EAW");
        guessAcronymsManager.handleGroupChat("C1", "U1", "Salah", "replyToken");
        assertEquals("guess EAW", guessAcronymsGroupState.getState());
        assertEquals(0, guessAcronymsUserState.getScore());
    }

    @Test
    void testPlayAcronymSkipAcronym() {
        GuessAcronymsUserState guessAcronymsUserState =
            new GuessAcronymsUserState("C1", "U1", "", 0);
        doReturn(Optional.of(guessAcronymsUserState))
            .when(guessAcronymsUserStateRepository)
            .findByGroupIdAndUserId("C1", "U1");
        GuessAcronymsGroupState guessAcronymsGroupState =
            new GuessAcronymsGroupState("C1", "guess EAW");
        doReturn(Optional.of(guessAcronymsGroupState))
            .when(guessAcronymsGroupStateRepository)
            .findByGroupId("C1");
        GuessAcronyms guessAcronym = new GuessAcronyms("EAW", "Endrawan Andika Wicaksana");
        doReturn(Optional.of(guessAcronym))
            .when(guessAcronymsRepository)
            .findByAcronym("EAW");
        List<GuessAcronyms> guessAcronyms = new ArrayList<>();
        GuessAcronyms guessAcronym2 = new GuessAcronyms("EA", "Endrawan Andika");
        guessAcronyms.add(guessAcronym2);
        doReturn(guessAcronyms)
            .when(guessAcronymsRepository)
            .findAll();
        guessAcronymsManager.handleGroupChat("C1", "U1", "next acronym", "replyToken");
        verify(lineMessagingClient, atLeastOnce())
            .replyMessage(new ReplyMessage("replyToken", new TextMessage("EA")));
        assertEquals("guess EA", guessAcronymsGroupState.getState());
        assertEquals(0, guessAcronymsUserState.getScore());
    }

    @Test
    void testPlayAcronymStopGameInGroup() {
        GuessAcronymsUserState guessAcronymsUserState =
            new GuessAcronymsUserState("C1", "U1", "", 0);
        doReturn(Optional.of(guessAcronymsUserState))
            .when(guessAcronymsUserStateRepository)
            .findByGroupIdAndUserId("C1", "U1");
        GuessAcronymsGroupState guessAcronymsGroupState =
            new GuessAcronymsGroupState("C1", "guess EAW");
        doReturn(Optional.of(guessAcronymsGroupState))
            .when(guessAcronymsGroupStateRepository)
            .findByGroupId("C1");
        List<GuessAcronymsUserState> guessAcronymsUserStates = new ArrayList<>();
        guessAcronymsUserStates.add(guessAcronymsUserState);
        doReturn(guessAcronymsUserStates)
            .when(guessAcronymsUserStateRepository)
            .findByGroupId("C1");
        guessAcronymsManager.handleGroupChat("C1", "U1", "stop acronym", "replyToken");
        verify(lineMessagingClient, atLeastOnce())
            .replyMessage(new ReplyMessage("replyToken", new TextMessage("1. Endrawan - 0")));
        assertEquals("", guessAcronymsGroupState.getState());
    }

    @Test
    void testPlayAcronymStopGameInRoom() {
        GuessAcronymsUserState guessAcronymsUserState =
            new GuessAcronymsUserState("R1", "U1", "", 0);
        doReturn(Optional.of(guessAcronymsUserState))
            .when(guessAcronymsUserStateRepository)
            .findByGroupIdAndUserId("R1", "U1");
        GuessAcronymsGroupState guessAcronymsGroupState =
            new GuessAcronymsGroupState("R1", "guess EAW");
        doReturn(Optional.of(guessAcronymsGroupState))
            .when(guessAcronymsGroupStateRepository)
            .findByGroupId("R1");
        List<GuessAcronymsUserState> guessAcronymsUserStates = new ArrayList<>();
        guessAcronymsUserStates.add(guessAcronymsUserState);
        doReturn(guessAcronymsUserStates)
            .when(guessAcronymsUserStateRepository)
            .findByGroupId("R1");
        guessAcronymsManager.handleGroupChat("R1", "U1", "stop acronym", "replyToken");
        verify(lineMessagingClient, atLeastOnce())
            .replyMessage(new ReplyMessage("replyToken", new TextMessage("1. Endrawan - 0")));
        assertEquals("", guessAcronymsGroupState.getState());
    }
}
