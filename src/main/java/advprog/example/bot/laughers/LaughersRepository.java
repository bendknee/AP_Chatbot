package advprog.example.bot.laughers;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LaughersRepository extends CrudRepository<Laughers, Long> {

    List<Laughers> findByGroupIdOrderByNumberOfLaughDesc(long groupId);
    Optional<Laughers> findByGroupIdAndUserId(long groupId, long userId);
}
