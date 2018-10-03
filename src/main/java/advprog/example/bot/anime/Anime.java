package advprog.example.bot.anime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Anime {

    String title;
    Date start;
    Date end;
    SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");

    public Anime(String title, String start, String end) {
        this.title = title;

        String[] startStr = start.split("-");
        int year = Integer.parseInt(startStr[0]);
        int month = Integer.parseInt(startStr[1]);
        int day = Integer.parseInt(startStr[2]);

        month = month == 0 ? 1 : month;
        day = day == 0 ? 1 : day;

        this.start = new GregorianCalendar(year, month - 1, day).getTime();

        if (!end.equalsIgnoreCase("0000-00-00")) {
            String[] endStr = end.split("-");
            year = Integer.parseInt(endStr[0]);
            month = Integer.parseInt(endStr[1]);
            day = Integer.parseInt(endStr[2]);

            month = month == 0 ? 1 : month;
            day = day == 0 ? 1 : day;

            this.end = new GregorianCalendar(year, month - 1, day).getTime();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatusMessage() {
        Date now = new Date();
        String message = "Oops, Something's wrong with our end";
        if (this.end != null && this.end.before(now)) {
            message =  "ANIME has finished airing at " + sf.format(this.end);
        } else if (this.start != null && this.start.after(now)) {
            message =  "ANIME will air starting at " + sf.format(this.start);
        } else if (this.start.before(now)) {
            if (this.end == null) {
                message = "ANIME is airing from " + sf.format(this.start) + " until unknown time";
            } else {
                message =  "ANIME is airing from " + sf.format(this.start)
                        + " until " + sf.format(this.end);
            }
        }

        return message;
    }


}
