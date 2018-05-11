package advprog.example.bot.laughers;

import com.linecorp.bot.client.LineMessagingClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LaughersManager {

    private LaughersRepository laughersRepository;
    private LineMessagingClient lineMessagingClient;

    @Autowired
    public LaughersManager(LaughersRepository laughersRepository,
                           LineMessagingClient lineMessagingClient) {
        this.laughersRepository = laughersRepository;
        this.lineMessagingClient = lineMessagingClient;
    }

    public boolean checkMessageContainsLaughers(String message) {
        return message.contains("wkwk") | message.contains("haha");
    }

    public void processMessage(String message, String groupId, String userId) {
        if (checkMessageContainsLaughers(message)) {
            Optional<Laughers> laughersOptional =
                laughersRepository.findByGroupIdAndUserId(groupId, userId);

            if (laughersOptional.isPresent()) {
                Laughers laughers = laughersOptional.get();
                laughers.incrementNumberOfLaugh();
                laughersRepository.save(laughers);
            } else {
                laughersRepository.save(new Laughers(groupId, userId));
            }
        }
    }

    public String getTop5Laughers(String groupId) {
        List<Laughers> laughersList =
            laughersRepository.findByGroupIdOrderByNumberOfLaughDesc(groupId);
        StringBuilder stringBuilder = new StringBuilder();

        long sumOfAllLaugh = laughersList.stream().mapToLong(Laughers::getNumberOfLaugh).sum();

        for (int i = 0; i < Math.min(5, laughersList.size()); i++) {
            if (i != 0) {
                stringBuilder.append("\n");
            }

            Laughers laughers = laughersList.get(i);

            stringBuilder.append(i + 1);
            stringBuilder.append(". ");

            try {
                switch (laughers.getGroupId().charAt(0)) {
                    case 'C':
                        stringBuilder.append(lineMessagingClient
                                                 .getGroupMemberProfile(groupId,
                                                                        laughers.getUserId())
                                                 .get().getDisplayName());
                        break;
                    case 'R':
                        stringBuilder.append(lineMessagingClient
                                                 .getRoomMemberProfile(groupId,
                                                                       laughers.getUserId())
                                                 .get().getDisplayName());
                        break;
                    case 'U':
                    default:
                        stringBuilder.append(lineMessagingClient
                                                 .getProfile(laughers.getUserId())
                                                 .get().getDisplayName());
                        break;
                }
            } catch (Exception e) {
                return "Error when get user profile name";
            }

            stringBuilder.append(" (");
            stringBuilder.append((int) ((double) laughers.getNumberOfLaugh() /
                (double) sumOfAllLaugh));
            stringBuilder.append("%)");
        }

        return stringBuilder.toString();
    }
}
