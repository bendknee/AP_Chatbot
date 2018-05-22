package advprog.example.bot.bible;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import advprog.example.bot.EventTestUtil;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;

import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;

public class BibleMessageHandlerTest {
    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    private static MessageEvent<TextMessageContent> validGroupChatMessageEvent =
            EventTestUtil.createDummyGroupTextMessage(
                    BibleMessageHandler.GROUP_CHAT_INPUT_SPEC
            );

    private static MessageEvent<TextMessageContent> invalidGroupChatMessageEvent =
            EventTestUtil.createDummyGroupTextMessage(
                    BibleMessageHandler.PRIVATE_CHAT_INPUT_SPEC
            );

    private static MessageEvent<TextMessageContent> validPrivateChatMessageEvent =
            EventTestUtil.createDummyTextMessage(
                    BibleMessageHandler.PRIVATE_CHAT_INPUT_SPEC + " Genesis 1:1"
            );

    private static MessageEvent<TextMessageContent> invalidPrivateChatMessageEvent =
            EventTestUtil.createDummyTextMessage(
                    BibleMessageHandler.GROUP_CHAT_INPUT_SPEC
            );

    @Test
    void testIsGroupEventTrue() {
        assertTrue(new BibleMessageHandler(validGroupChatMessageEvent).isGroupMessageEvent());
        assertTrue(new BibleMessageHandler(invalidGroupChatMessageEvent).isGroupMessageEvent());
    }

    @Test
    void testIsGroupEventFalse() {
        assertFalse(new BibleMessageHandler(validPrivateChatMessageEvent).isGroupMessageEvent());
        assertFalse(new BibleMessageHandler(invalidPrivateChatMessageEvent).isGroupMessageEvent());
    }

    @Test
    void testGetUserId() {
        assertEquals("userId", new BibleMessageHandler(validPrivateChatMessageEvent).getUserId());
    }

    @Test
    void testGetGroupId() {
        assertEquals("groupId", new BibleMessageHandler(validGroupChatMessageEvent).getGroupId());
    }


    @Test
    void testShouldHandleTextMessageEventBecauseOfMatchingGroupChatInputSpec() {
        assertTrue(new BibleMessageHandler(validGroupChatMessageEvent).shouldHandleMessageEvent());
    }

    @Test
    void testShouldHandleTextMessageEventBecauseOfMatchingPrivateChatInputSpec() {
        assertTrue(new BibleMessageHandler(validPrivateChatMessageEvent)
                .shouldHandleMessageEvent());
    }

    @Test
    void testShouldHandleMessageEventFalse() {
        assertFalse(new BibleMessageHandler(invalidGroupChatMessageEvent)
                .shouldHandleMessageEvent());
        assertFalse(new BibleMessageHandler(invalidPrivateChatMessageEvent)
                .shouldHandleMessageEvent());
    }

    @Test
    void testHandleTextMessageEventShouldCallGetGroupChatReplyMessage() throws Exception {
        BibleMessageHandler handler = spy(new BibleMessageHandler(validGroupChatMessageEvent));
        assertTrue(handler.isGroupMessageEvent());
        assertTrue(handler.shouldHandleMessageEvent());
        handler.handleTextMessageEvent();
        verify(handler, atLeastOnce()).getGroupChatReplyMessage();
    }

    @Test
    void testHandleTextMessageEventShouldCallGetPrivateChatReplyMessage() throws Exception {
        BibleMessageHandler handler = spy(new BibleMessageHandler(validPrivateChatMessageEvent));
        assertTrue(handler.shouldHandleMessageEvent());
        assertFalse(handler.isGroupMessageEvent());
        handler.handleTextMessageEvent();
        verify(handler, atLeastOnce()).getPrivateChatReplyMessage();
    }

    @Test
    void testHandleTextMessageEventShouldThrowExceptionOnInvalidPrivateChatMessage() {
        Executable toTest = () ->
                new BibleMessageHandler(invalidPrivateChatMessageEvent).handleTextMessageEvent();
        assertThrows(AssertionError.class, toTest);
    }

    @Test
    void testHandleTextMessageEventShouldThrowExceptionOnInvalidGroupChatMessage() {
        BibleMessageHandler.lastMessageBookName.remove("groupId");
        Executable toTest = () ->
                new BibleMessageHandler(invalidGroupChatMessageEvent).handleTextMessageEvent();
        assertThrows(AssertionError.class, toTest);
    }

    @Test
    void testGetGroupChatReplyMessageOnCorrectGuessingLastGeneratedVerse() {
        BibleMessageHandler handler = new BibleMessageHandler(
                EventTestUtil.createDummyGroupTextMessage(
                    BibleMessageHandler.GROUP_CHAT_INPUT_SPEC
                )
        );
        handler.handleTextMessageEvent();
        String lastMessageBookName = BibleMessageHandler.lastMessageBookName.get("groupId");
        handler = new BibleMessageHandler(
                EventTestUtil.createDummyGroupTextMessage(lastMessageBookName)
        );
        TextMessage replyMessage = (TextMessage) handler.handleTextMessageEvent();
        assertTrue(replyMessage
                .getText()
                .contains(BibleMessageHandler.GUESS_CORRECT_REPLY_MESSAGE_CONTENT)
        );
        assertNotEquals(lastMessageBookName, BibleMessageHandler.lastMessageBookName);
    }

    @Test
    void testGetGroupChatReplyMessageOnWrongGuessingLastGeneratedVerse() {
        BibleMessageHandler handler = new BibleMessageHandler(
                EventTestUtil.createDummyGroupTextMessage(
                        BibleMessageHandler.GROUP_CHAT_INPUT_SPEC
                )
        );
        handler.handleTextMessageEvent();
        String content = "halo albertus angga";
        assertNotEquals(content, BibleMessageHandler.lastMessageBookName.get("groupId"));
        handler = new BibleMessageHandler(
                EventTestUtil.createDummyGroupTextMessage(content)
        );
        TextMessage replyMessage = (TextMessage) handler.handleTextMessageEvent();
        assertTrue(replyMessage
                .getText()
                .contains(BibleMessageHandler.GUESS_WRONG_REPLY_MESSAGE_CONTENT)
        );
    }

    @Test
    void testGetGroupChatReplyMessageShouldThrowsExceptionOnPrivateChat() {
        assertThrows(AssertionError.class, () -> {
            new BibleMessageHandler(EventTestUtil.createDummyTextMessage(
               BibleMessageHandler.GROUP_CHAT_INPUT_SPEC
            )).getGroupChatReplyMessage();
        });
    }

    @Test
    void testGetPrivateChatReplyMessageShouldThrowsExceptionOnGroupChat() {
        assertThrows(AssertionError.class, () -> {
            new BibleMessageHandler(EventTestUtil.createDummyGroupTextMessage(
                    BibleMessageHandler.PRIVATE_CHAT_INPUT_SPEC
            )).getPrivateChatReplyMessage();
        });
    }

    @Test
    void testGetPrivateChatReplyMessageShouldReturnCarouselOfBooks() {
        MessageEvent<TextMessageContent> event = EventTestUtil.createDummyTextMessage(
                "/bible"
        );
        BibleMessageHandler handler = new BibleMessageHandler(event);
        Message replyMessage = handler.handleTextMessageEvent();
        assertTrue(replyMessage instanceof TemplateMessage);
        CarouselTemplate template = (CarouselTemplate)((TemplateMessage)replyMessage).getTemplate();
        for (CarouselColumn column: template.getColumns()) {
            assertTrue(handler.bibleVerseFactory.isBookExist(column.getText()));
        }
    }

    @Test
    void testGetPrivateChatReplyMessageShouldReturnCarouselOfChapters() {
        MessageEvent<TextMessageContent> event = EventTestUtil.createDummyTextMessage(
                "/bible Genesis"
        );
        BibleMessageHandler handler = new BibleMessageHandler(event);
        Message replyMessage = handler.handleTextMessageEvent();
        assertTrue(replyMessage instanceof TemplateMessage);
        CarouselTemplate template = (CarouselTemplate)((TemplateMessage)replyMessage).getTemplate();
        for (CarouselColumn column: template.getColumns()) {
            String[] split = column.getText().split(" ");
            String book = split[0];
            String chapter = split[1];
            assertTrue(handler.bibleVerseFactory.isChapterExist(book, chapter));
        }
    }

    @Test
    void testShouldHandleManualVerseInput() {
        BibleMessageHandler.choosenBook.remove("userId");
        BibleMessageHandler.choosenChapter.remove("userId");
        new BibleMessageHandler(EventTestUtil.createDummyTextMessage("/bible"))
                .handleTextMessageEvent();
        new BibleMessageHandler(EventTestUtil.createDummyTextMessage("/bible Genesis"))
                .handleTextMessageEvent();
        new BibleMessageHandler(EventTestUtil.createDummyTextMessage("/bible Genesis 1"))
                .handleTextMessageEvent();
        BibleMessageHandler handler =
                new BibleMessageHandler(EventTestUtil.createDummyTextMessage("1"));
        assertTrue(handler.shouldHandleManualVerseInput());
        TextMessage replyMessage = (TextMessage)handler.handleTextMessageEvent();
        assertTrue(replyMessage.getText().contains(
                "In the beginning God created the heavens and the earth."));
    }

    @Test
    void testGetPrivateChatReplyMessageOnCompleteQuery() throws Exception {
        MessageEvent<TextMessageContent> event = EventTestUtil.createDummyTextMessage(
                "/bible Genesis 1:1"
        );
        BibleMessageHandler handler = new BibleMessageHandler(event);
        Message replyMessage = handler.handleTextMessageEvent();
        assertTrue(replyMessage instanceof TextMessage);
        String actual = ((TextMessage)replyMessage).getText();
        String expected = "In the beginning God created the heavens and the earth.";
        assertTrue(actual.contains(expected));

        event = EventTestUtil.createDummyTextMessage(
                "/bible Genesis 42:3"
        );
        handler = new BibleMessageHandler(event);
        actual = ((TextMessage)handler.handleTextMessageEvent()).getText();
        expected = "Then ten of Joseph's brothers went down to buy grain from Egypt.";
        assertTrue(actual.contains(expected));
    }

}