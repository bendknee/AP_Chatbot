package advprog.example.bot.command.mediawiki;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum MediaWikiDao {

    INSTANCE();

    private String dataLocation = "mediawiki_data.csv";

    private static final String[] HEADERS = {
            "api_url",
    };

    private List<MediaWiki> mediawikis;

    MediaWikiDao() {
        loadCsv(dataLocation);
    }

    public List<MediaWiki> getMediaWikis() {
        return new ArrayList<>(mediawikis);
    }

    public MediaWiki persistMediaWiki(MediaWiki mediaWiki) {
        if (mediaWiki == null) {
            throw new IllegalArgumentException("mediawiki must not be null");
        }

        mediawikis.add(mediaWiki);
        this.writeCsv(dataLocation);
        return mediaWiki;
    }

    private synchronized void loadCsv(String csvPath) {
        mediawikis = new ArrayList<>();

        try (Reader in = new FileReader(csvPath)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withHeader(HEADERS)
                    .withFirstRecordAsHeader()
                    .parse(in);

            for (CSVRecord record : records) {
                mediawikis.add(new MediaWiki(record.get("api_url")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void writeCsv(String csvPath) {
        try (Writer out = new FileWriter(csvPath)) {
            CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withRecordSeparator("\n"));

            printer.printRecord(Arrays.asList(HEADERS));

            for (MediaWiki mediaWiki : mediawikis) {
                printer.printRecord(Collections.singletonList(mediaWiki.getApiUrl()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
