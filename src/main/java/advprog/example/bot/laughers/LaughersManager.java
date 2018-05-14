package advprog.example.bot.laughers;

import com.linecorp.bot.client.LineMessagingClient;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        message = message.toLowerCase();
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

        int[] percentage = new int[5];
        for (int i = 0; i < Math.min(5, laughersList.size()); i++) {
            Laughers laughers = laughersList.get(i);
            percentage[i] = (int) ((double) laughers.getNumberOfLaugh() * 100.0
                / (double) sumOfAllLaugh);
        }

        for (int i = 0; i < 5; i++) {
            if (i != 0) {
                stringBuilder.append("\n");
            }

            if (i < laughersList.size()) {
                Laughers laughers = laughersList.get(i);

                int rank = i;
                for (int j = i - 1; j >= 0; j--) {
                    if (percentage[i] == percentage[j]) {
                        rank = j;
                    }
                }
                stringBuilder.append(rank + 1);
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
                stringBuilder.append(percentage[i]);
                stringBuilder.append("%)");
            } else {
                stringBuilder.append(i + 1);
                stringBuilder.append(". ");
            }
        }

        return stringBuilder.toString();
    }
}
