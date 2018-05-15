package advprog.example.bot.laughers;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.profile.MembersIdsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private List<String> getActiveUser(char groupType, String groupId) throws Exception {
        List<String> activeUser = new ArrayList<>();
        String start = null;
        boolean canGetMoreMember = true;
        MembersIdsResponse membersIdsResponse;

        switch (groupType) {
            case 'C':
                while (canGetMoreMember) {
                    membersIdsResponse = lineMessagingClient.getGroupMembersIds(groupId, start)
                                                            .get();
                    activeUser.addAll(membersIdsResponse.getMemberIds());
                    canGetMoreMember = membersIdsResponse.getNext().isPresent();
                    if (canGetMoreMember) {
                        start = membersIdsResponse.getNext().get();
                    }
                }
                break;
            case 'R':
                while (canGetMoreMember) {
                    membersIdsResponse = lineMessagingClient.getRoomMembersIds(groupId, start)
                                                            .get();
                    activeUser.addAll(membersIdsResponse.getMemberIds());
                    canGetMoreMember = membersIdsResponse.getNext().isPresent();
                    if (canGetMoreMember) {
                        start = membersIdsResponse.getNext().get();
                    }
                }
                break;
            case 'U':
            default:
                activeUser.add(groupId);
                break;
        }

        return activeUser;
    }

    private int[] getTop5Percentage(List<Laughers> laughersList) {
        int[] percentage = new int[5];

        long sumOfAllLaugh = laughersList.stream().mapToLong(Laughers::getNumberOfLaugh).sum();
        for (int i = 0; i < Math.min(5, laughersList.size()); i++) {
            Laughers laughers = laughersList.get(i);
            percentage[i] = (int) ((double) laughers.getNumberOfLaugh() * 100.0
                / (double) sumOfAllLaugh);
        }

        return percentage;
    }

    private int getRank(int position, int[] percentage) {
        int rank = position;

        for (int j = position - 1; j >= 0; j--) {
            if (percentage[position] == percentage[j]) {
                rank = j;
            }
        }

        return rank + 1;
    }

    private String getDisplayName(char groupType, String groupId, String userId) throws Exception {
        switch (groupType) {
            case 'C':
                return lineMessagingClient.getGroupMemberProfile(groupId, userId)
                                          .get()
                                          .getDisplayName();
            case 'R':
                return lineMessagingClient.getRoomMemberProfile(groupId, userId)
                                          .get()
                                          .getDisplayName();
            case 'U':
            default:
                return lineMessagingClient.getProfile(userId)
                                          .get()
                                          .getDisplayName();
        }
    }

    public String getTop5Laughers(String groupId) {
        char groupType = groupId.charAt(0);

        List<String> activeUser;
        try {
            activeUser = getActiveUser(groupType, groupId);
        } catch (Exception e) {
            return "Error when get members";
        }

        List<Laughers> laughersList =
            laughersRepository.findByGroupIdOrderByNumberOfLaughDesc(groupId)
                              .stream()
                              .filter(laughers -> activeUser.contains(laughers.getUserId()))
                              .collect(Collectors.toList());

        int[] percentage = getTop5Percentage(laughersList);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i != 0) {
                stringBuilder.append("\n");
            }

            if (i < laughersList.size()) {
                stringBuilder.append(getRank(i, percentage));
                stringBuilder.append(". ");

                try {
                    stringBuilder.append(getDisplayName(groupType,
                                                        groupId,
                                                        laughersList.get(i).getUserId()));
                } catch (Exception e) {
                    stringBuilder.append("Error when get user profile name");
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
