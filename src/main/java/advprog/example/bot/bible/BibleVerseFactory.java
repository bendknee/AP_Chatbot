package advprog.example.bot.bible;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class BibleVerseFactory {

    public File jsonFile;
    public JsonNode bibleDatabase;

    public BibleVerseFactory() throws IOException {
        String pathName = new File("").getAbsolutePath();
        pathName = pathName.concat("/src/main/java/advprog/example/bot/bible/bible.json");
        jsonFile = new File(pathName);
        bibleDatabase = new ObjectMapper().readTree(jsonFile);
    }

    public boolean isBookExist(String book) {
        Iterator<String> booksIterator = bibleDatabase.fieldNames();
        while (booksIterator.hasNext()) {
            if (booksIterator.next().equals(capitalize(book))) {
                return true;
            }
        }
        return false;
    }

    public boolean isChapterExist(String book, String chapter) {
        assert isBookExist(book);
        Iterator<String> chaptersIterator = bibleDatabase.get(capitalize(book))
                .fieldNames();
        while (chaptersIterator.hasNext()) {
            if (chaptersIterator.next().equalsIgnoreCase(chapter)) {
                return true;
            }
        }
        return false;
    }

    public boolean isVerseNumberExist(String book, String chapter, String verse) {
        assert isBookExist(book);
        assert isChapterExist(book, chapter);
        Iterator<String> verseNumbersIterator = bibleDatabase.get(capitalize(book))
                .get(chapter).fieldNames();
        while (verseNumbersIterator.hasNext()) {
            if (verseNumbersIterator.next().equalsIgnoreCase(verse)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getBooks() {
        List<String> books = new ArrayList<>();
        Iterator<String> booksIterator = bibleDatabase.fieldNames();
        while (booksIterator.hasNext()) {
            books.add(booksIterator.next());
        }
        Collections.sort(books);
        return books;
    }

    public List<String> getChapters(String book) {
        assert isBookExist(book);
        List<String> chapters = new ArrayList<>();
        Iterator<String> chaptersIterator = bibleDatabase.get(capitalize(book))
                .fieldNames();
        while (chaptersIterator.hasNext()) {
            chapters.add(chaptersIterator.next());
        }
        Collections.sort(chapters,
            (String v1, String v2) -> Integer.parseInt(v1) - Integer.parseInt(v2));
        return chapters;
    }

    public List<String> getVerseNumbers(String book, String chapter) {
        assert isBookExist(book);
        assert isChapterExist(book, chapter);
        List<String> verses = new ArrayList<>();
        Iterator<String> versesIterator = bibleDatabase.get(capitalize(book))
                .get(chapter).fieldNames();
        while (versesIterator.hasNext()) {
            verses.add(versesIterator.next());
        }
        Collections.sort(verses,
            (String v1, String v2) -> Integer.parseInt(v1) - Integer.parseInt(v2));
        return verses;
    }

    public static String capitalize(String in) {
        return in.substring(0, 1).toUpperCase() + in.substring(1).toLowerCase();
    }

    public String getVerse(String book, String chapter, String verseNumber) throws Exception {
        if (!isBookExist(book)) {
            String error = String.format("Book %s doesn't exist in the database. Please try ",
                    book);
            for (String availableBook: getBooks()) {
                error += availableBook + ", ";
            }
            throw new Exception(error);
        }
        if (!isChapterExist(book, chapter)) {
            String error = String.format("Chapter %s is not a valid chapter in book %s.",
                    chapter, book);
            error += String.format("\nValid chapters are from %s to %s",
                    getChapters(book).get(0),
                    getChapters(book).get(getChapters(book).size() - 1));
            throw new Exception(error);
        }
        if (!isVerseNumberExist(book, chapter, verseNumber)) {
            String error = String.format("Verse number %s is not a valid verse number in %s %s.",
                    verseNumber, book, chapter);
            error += String.format("\nValid verse numbers are from %s to %s",
                    getVerseNumbers(book, chapter).get(0),
                    getVerseNumbers(book, chapter).get(getVerseNumbers(book, chapter).size() - 1));
            throw new Exception(error);
        }
        return String.format("\"%s\"",
                bibleDatabase.get(capitalize(book)).get(chapter).get(verseNumber).asText());
    }

    public String getIteratorElement(Iterator<String> iterator, int index) {
        int i = 0;
        while (iterator.hasNext()) {
            if (i == index) {
                return iterator.next();
            }
            iterator.next();
            i++;
        }
        return "";
    }

    public String getRandomBook() {
        int booksLength = getBooks().size();
        int randomIndex = new Random(System.currentTimeMillis()).nextInt(booksLength);
        Iterator<String> booksIterator = bibleDatabase.fieldNames();
        return getIteratorElement(booksIterator, randomIndex);
    }

    public String getRandomChapter(String book) {
        int chaptersLength = getChapters(book).size();
        int randomIndex = new Random(System.currentTimeMillis()).nextInt(chaptersLength);
        Iterator<String> chaptersIterator = bibleDatabase.get(capitalize(book))
                .fieldNames();
        return getIteratorElement(chaptersIterator, randomIndex);
    }

    public String getRandomVerseNumber(String book, String chapter) {
        int verseLength = getVerseNumbers(book, chapter).size();
        int randomIndex = new Random(System.currentTimeMillis()).nextInt(verseLength);
        Iterator<String> verseNumbersIterator = bibleDatabase.get(capitalize(book))
                .get(chapter).fieldNames();
        return getIteratorElement(verseNumbersIterator, randomIndex);
    }

}
