package advprog.example.bot.laughers;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LaughersRepository extends CrudRepository<Laughers, Long> {

    List<Laughers> findByGroupIdOrderByNumberOfLaughDesc(String groupId);
    Optional<Laughers> findByGroupIdAndUserId(String groupId, String userId);
}
