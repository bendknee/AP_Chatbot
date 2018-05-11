package advprog.example.bot.laughers;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LaughersRepository extends CrudRepository<Laughers, Long> {

    List<Laughers> findByGroupId(long groupId);
}
