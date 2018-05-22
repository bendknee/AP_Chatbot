package advprog.example.bot.bible;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.test.context.TestExecutionListeners;


public class BibleVerseFactoryTest {
    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    private static BibleVerseFactory factory;

    @BeforeAll
    static void setUp() throws Exception {
        factory = new BibleVerseFactory();
    }

    @Test
    void testConstructorDoesNotThrowException() {
        assertDoesNotThrow(BibleVerseFactory::new);
    }

    @Test
    void testIsBookExistTrue() {
        assertTrue(factory.isBookExist("Genesis"));
        assertTrue(factory.isBookExist("Matthew"));
    }

    @Test
    void testIsBookExistFalse() {
        assertFalse(factory.isBookExist("Albertus"));
        assertFalse(factory.isBookExist("Kautsar Fadlillah"));
    }

    @Test
    void testIsChapterExistTrue() {
        assertTrue(factory.isChapterExist("Genesis", "1"));
        assertTrue(factory.isChapterExist("Genesis", "42"));
        assertTrue(factory.isChapterExist("Matthew", "1"));
    }

    @Test
    void testIsChapterExistFalse() {
        assertFalse(factory.isChapterExist("Genesis", "110"));
        assertFalse(factory.isChapterExist("Matthew", "123"));
    }

    @Test
    void testIsChapterExistThrowsAssertionError() {
        assertThrows(AssertionError.class, () -> factory.isChapterExist("Quran", "1"));
    }

    @Test
    void testIsVerseNumberExistTrue() {
        assertTrue(factory.isVerseNumberExist("Genesis", "1", "1"));
        assertTrue(factory.isVerseNumberExist("Matthew", "1", "2"));
    }

    @Test
    void testIsVerseNumberExistFalse() {
        assertFalse(factory.isVerseNumberExist("Genesis", "1", "1000"));
    }

    @Test
    void testIsVerseNumberExistThrowsAssertionError() {
        assertThrows(AssertionError.class, () -> factory.isVerseNumberExist("Quran", "1", "1"));
        assertThrows(AssertionError.class,
            () -> factory.isVerseNumberExist("Genesis", "110", "1")
        );
    }

    @Test
    void testGetRandomBook() throws InterruptedException {
        String random1 = factory.getRandomBook();
        String random2 = factory.getRandomBook();
        for (int i = 0; i < 1000000; ++i) {
            if (!random2.equals(random1)) {
                break;
            }
            // Sleep 1 millisecond to generate new Random seed
            TimeUnit.MILLISECONDS.sleep(1);
            random2 = factory.getRandomBook();
        }
        assertTrue(factory.isBookExist(random1));
        assertTrue(factory.isBookExist(random2));
        assertNotEquals(random1, random2);
    }

    @Test
    void testGetRandomChapter() throws InterruptedException {
        assertThrows(AssertionError.class, () -> factory.getRandomChapter("Quran"));
        String randomBook = factory.getRandomBook();
        String randomChapter1 = factory.getRandomChapter(randomBook);
        String randomChapter2 = factory.getRandomChapter(randomBook);
        for (int i = 0; i < 1000000; ++i) {
            if (!randomChapter2.equals(randomChapter1)) {
                break;
            }
            // Sleep 1 millisecond to generate new random seed
            TimeUnit.MILLISECONDS.sleep(1);
            randomChapter2 = factory.getRandomChapter(randomBook);
        }
        assertTrue(factory.isChapterExist(randomBook, randomChapter1));
        assertTrue(factory.isChapterExist(randomBook, randomChapter2));
        assertNotEquals(randomChapter1, randomChapter2);
    }

    @Test
    void testGetRandomVerseNumber() throws InterruptedException {
        assertThrows(AssertionError.class, () -> factory.getRandomVerseNumber("Quran", "1000"));
        String randomBook = factory.getRandomBook();
        assertThrows(AssertionError.class, () -> factory.getRandomVerseNumber(randomBook, "10000"));
        String randomChapter = factory.getRandomChapter(randomBook);
        String randomVerse1 = factory.getRandomVerseNumber(randomBook, randomChapter);
        String randomVerse2 = factory.getRandomVerseNumber(randomBook, randomChapter);
        for (int i = 0; i < 1000000; ++i) {
            if (!randomVerse2.equals(randomVerse1)) {
                break;
            }
            // Sleep 1 millisecond to generate new random seed
            TimeUnit.MILLISECONDS.sleep(1);
            randomVerse2 = factory.getRandomVerseNumber(randomBook, randomChapter);
        }
        assertTrue(factory.isVerseNumberExist(randomBook, randomChapter, randomVerse1));
        assertTrue(factory.isVerseNumberExist(randomBook, randomChapter, randomVerse2));
        assertNotEquals(randomVerse1, randomVerse2);
    }

    @Test
    void testGetBooks() {
        List<String> books = factory.getBooks();
        for (String book: books) {
            assertTrue(factory.isBookExist("Genesis"));
        }
    }

    @Test
    void testGetChapters() {
        String randomBook = factory.getRandomBook();
        List<String> chapters = factory.getChapters(randomBook);
        for (String chapter: chapters) {
            assertTrue(factory.isChapterExist(randomBook, chapter));
        }
    }

    @Test
    void testGetVerseNumbers() {
        String randomBook = factory.getRandomBook();
        String randomChapter = factory.getRandomChapter(randomBook);
        List<String> verseNumbers = factory.getVerseNumbers(randomBook, randomChapter);
        for (String verseNumber: verseNumbers) {
            assertTrue(factory.isVerseNumberExist(randomBook, randomChapter, verseNumber));
        }
    }

    @Test
    void testGetVerseShouldThrowExceptionOnInvalidBook() {
        assertThrows(Exception.class, () -> factory.getVerse("Quran", "1", "1"));
    }

    @Test
    void testGetVerseShouldThrowExceptionOnInvalidChapter() {
        assertThrows(Exception.class, () -> factory.getVerse("Genesis", "110", "1"));
    }

    @Test
    void testGetVerseShouldThrowExceptionOnInvalidVerseNumber() {
        assertThrows(Exception.class, () -> factory.getVerse("Genesis", "1", "110"));
    }

    @Test
    void testGetVerse() throws Exception {
        String actual = factory.getVerse("Genesis", "1", "1");
        String expected = "In the beginning God created the heavens and the earth.";
        assertTrue(actual.contains(expected));

        actual = factory.getVerse("Genesis", "42", "3");
        expected = "Then ten of Joseph's brothers went down to buy grain from Egypt.";
        assertTrue(actual.contains(expected));
    }
}