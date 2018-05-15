package advprog.example.bot.billboard.tropical;

import advprog.example.bot.Command;
import advprog.example.bot.billboard.tropical.objects.Song;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import java.util.List;

public class BillboardTropicalCommand implements Command {
    @Override
    public Message produceMessage(MessageContent content) {
        List<Song> songs = Scrapper.getInstance().getChart();
        String replyText = "";

        for (Song song : songs) {
            replyText += song.toString() + "\n";
        }

        return new TextMessage(replyText.substring(0, replyText.length() - 1));
    }
}
