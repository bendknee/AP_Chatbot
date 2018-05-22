package advprog.example.bot.repository;

import advprog.example.bot.model.GuessAcronymsGroupState;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuessAcronymsGroupStateRepository
    extends CrudRepository<GuessAcronymsGroupState, Long> {

    Optional<GuessAcronymsGroupState> findByGroupId(String groupId);
}
