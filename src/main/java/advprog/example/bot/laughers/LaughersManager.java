package advprog.example.bot.laughers;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class LaughersManager {

    private LaughersRepository laughersRepository;

    @Autowired
    public LaughersManager(LaughersRepository laughersRepository) {
        this.laughersRepository = laughersRepository;
    }

    public boolean checkMessageContainsLaughers(String message) {
        return message.contains("wkwk") | message.contains("haha");
    }

    public void processMessage(String message, long groupId, long userId) {
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

    public String getTop5LaughersInGroup(long groupId) {
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
            stringBuilder.append(laughers.getUserId());
            stringBuilder.append(" (");
            stringBuilder.append((int) ((double) laughers.getNumberOfLaugh() / (double) sumOfAllLaugh));
            stringBuilder.append("%)");
        }

        return stringBuilder.toString();
    }
}
