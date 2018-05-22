package advprog.example.bot.repository;

import advprog.example.bot.model.GuessAcronymsUserState;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuessAcronymsUserStateRepository
    extends CrudRepository<GuessAcronymsUserState, Long> {

    Optional<GuessAcronymsUserState> findByGroupIdAndUserId(String groupId, String userId);

    List<GuessAcronymsUserState> findByGroupId(String groupId);
}
