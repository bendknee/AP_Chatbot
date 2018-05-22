package advprog.example.bot.bible;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.Template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BibleMessageHandler {
    public static final String PRIVATE_CHAT_INPUT_SPEC = "/bible";
    public static final String GROUP_CHAT_INPUT_SPEC = "gospel".toLowerCase();

    public static final String GUESS_CORRECT_REPLY_MESSAGE_CONTENT = "You are correct";
    public static final String GUESS_WRONG_REPLY_MESSAGE_CONTENT = "Try again";

    public static Map<String, String> lastMessageBookName = new HashMap<>();

    public static Map<String, String> choosenBook = new HashMap<>();
    public static Map<String, String> choosenChapter = new HashMap<>();

    public BibleVerseFactory bibleVerseFactory;
    public MessageEvent<TextMessageContent> event;
    public String chatMessageContent;

    public BibleMessageHandler(MessageEvent<TextMessageContent> event) {
        this.event = event;
        this.chatMessageContent = event.getMessage().getText();
        try {
            bibleVerseFactory = new BibleVerseFactory();
        } catch (IOException e) {
            bibleVerseFactory = null;
        }
    }

    public boolean isGroupMessageEvent() {
        Source source = event.getSource();
        return source instanceof GroupSource;
    }

    public String getGroupId() {
        assert isGroupMessageEvent();
        return ((GroupSource) event.getSource()).getGroupId();
    }

    public String getUserId() {
        return event.getSource().getUserId();
    }

    public boolean shouldHandleManualVerseInput() {
        String userId = event.getSource().getUserId();
        return choosenBook.containsKey(userId) && choosenChapter.containsKey(userId);
    }

    public boolean shouldHandleMessageEvent() {
        if (isGroupMessageEvent()) {
            return chatMessageContent.toLowerCase().contains(GROUP_CHAT_INPUT_SPEC)
                    || lastMessageBookName.containsKey(getGroupId());
        }
        return chatMessageContent.toLowerCase()
                .contains(PRIVATE_CHAT_INPUT_SPEC) || shouldHandleManualVerseInput();
    }

    public Message getPrivateChatReplyMessage() throws Exception {
        assert !isGroupMessageEvent();
        if (shouldHandleManualVerseInput()) {
            String book = choosenBook.get(getUserId());
            String chapter = choosenChapter.get(getUserId());
            String verseNumber = chatMessageContent;
            if (bibleVerseFactory.isVerseNumberExist(book, chapter, verseNumber)) {
                choosenBook.remove(getUserId());
                choosenChapter.remove(getUserId());
            }
            return new TextMessage(bibleVerseFactory.getVerse(book, chapter, verseNumber));
        }
        String[] splittedChatMessageContent = chatMessageContent.split(" ");
        if (chatMessageContent.equals(PRIVATE_CHAT_INPUT_SPEC)) {
            // Send carousels of all books
            List<String> books = bibleVerseFactory.getBooks();
            List<CarouselColumn> columns = new ArrayList<>();
            for (String book : books) {
                List<Action> actions = new ArrayList<>();
                actions.add(new MessageAction("Choose", String.format("/bible %s", book)));
                columns.add(new CarouselColumn(null, book, book, actions));
            }
            Template carouselTemplate = new CarouselTemplate(columns);
            return new TemplateMessage("Chapters", carouselTemplate);
        } else {
            String book = splittedChatMessageContent[1];
            if (!bibleVerseFactory.isBookExist(book)) {
                throw new Exception(String.format("Book %s doesn't exist in the database.", book));
            }
            if (splittedChatMessageContent.length == 2) {
                // Send carousels of all chapters
                List<String> chapters = bibleVerseFactory.getChapters(book);
                Collections.shuffle(chapters);
                List<CarouselColumn> columns = new ArrayList<>();
                for (int i = 0; i < Math.min(chapters.size(), 10); ++i) {
                    List<Action> actions = new ArrayList<>();
                    actions.add(new MessageAction(
                        "Choose", String.format("/bible %s %s", book, chapters.get(i))));
                    columns.add(new CarouselColumn(null, book + " " + chapters
                            .get(i), book + " " + chapters.get(i), actions));
                }
                Template carouselTemplate = new CarouselTemplate(columns);
                return new TemplateMessage("Chapters", carouselTemplate);
            } else if (!splittedChatMessageContent[2].contains(":")) {
                choosenBook.put(getUserId(), 
                    splittedChatMessageContent[1]);
                choosenChapter.put(getUserId(), splittedChatMessageContent[2]);
                List<String> verses = bibleVerseFactory
                        .getVerseNumbers(splittedChatMessageContent[1], 
                                splittedChatMessageContent[2]);
                String output = String.format("Please input verse number manually for %s %s.",
                        splittedChatMessageContent[1], 
                        splittedChatMessageContent[2]);
                output += String.format("\nValid verse numbers are from %s to %s.", verses.get(0),
                        verses.get(verses.size() - 1));
                output += String.format("\nExample input: \"%s\" (without quotes)", verses
                        .get(verses.size() - 1));
                return new TextMessage(output);
            }
            String chapterAndVerse = splittedChatMessageContent[2];
            String chapter = chapterAndVerse.substring(0, chapterAndVerse.indexOf(":"));
            String verseNumber = chapterAndVerse.substring(chapterAndVerse.indexOf(":") + 1);
            return new TextMessage(bibleVerseFactory.getVerse(book, chapter, verseNumber));
        }
    }

    public Message getGroupChatReplyMessage() throws Exception {
        assert isGroupMessageEvent();
        String replyMessageContent;
        if (chatMessageContent.toLowerCase()
            .contains(GROUP_CHAT_INPUT_SPEC)) {
            String randomBook = bibleVerseFactory.getRandomBook();
            String randomChapter = bibleVerseFactory
                    .getRandomChapter(randomBook);
            String randomVerseNumber = bibleVerseFactory
                    .getRandomVerseNumber(randomBook, 
                            randomChapter);
            replyMessageContent = bibleVerseFactory
                    .getVerse(randomBook, randomChapter, randomVerseNumber);
            lastMessageBookName.put(getGroupId(), randomBook);
        } else {
            if (chatMessageContent.toLowerCase().contains(lastMessageBookName
                    .get(getGroupId()).toLowerCase())) {
                replyMessageContent = GUESS_CORRECT_REPLY_MESSAGE_CONTENT;
                lastMessageBookName.remove(getGroupId());
            } else {
                replyMessageContent = GUESS_WRONG_REPLY_MESSAGE_CONTENT;
            }
        }
        return new TextMessage(replyMessageContent);
    }

    public Message handleTextMessageEvent() {
        assert shouldHandleMessageEvent();
        Message replyMessage = null;
        try {
            if (isGroupMessageEvent()) {
                replyMessage = getGroupChatReplyMessage();
            } else {
                replyMessage = getPrivateChatReplyMessage();
            }
        } catch (Exception e) {
            replyMessage = new TextMessage(e.getMessage());
        }
        return replyMessage;
    }

}
