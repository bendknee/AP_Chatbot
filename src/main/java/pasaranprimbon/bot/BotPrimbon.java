package pasaranprimbon.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotExampleApplication {

    private static final Logger LOGGER = Logger.getLogger(BotExampleApplication.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotExampleApplication.class, args);
    }

    public int dayDifferenceGetter(String tanggal) {
        
    }

    public String dayGetter(int dayDifference) {
        ArrayList<String> dayList = new ArrayList<String>() {{
            add("Rabu");
            add("Kamis");
            add("Jumat");
            add("Sabtu");
            add("Minggu");
            add("Senin");
            add("Selasa");
        }};

        int day = dayDifference % 7;
        String dayName = dayList.get(day);

        return dayName;
    }

    public String pasaranGetter(int dayDifference) {
        ArrayList<String> pasaranList = new ArrayList<String>() {{
            add("Pon");
            add("Wage");
            add("Kliwon");
            add("Legi");
            add("Pahing");
        }};

        int pasaran = dayDifference % 5;
        String pasaranName = pasaranList.get(pasaran);

        return pasaranName;
    }
}