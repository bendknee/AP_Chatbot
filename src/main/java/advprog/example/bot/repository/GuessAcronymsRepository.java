package advprog.example.bot.repository;

import advprog.example.bot.model.GuessAcronyms;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuessAcronymsRepository extends CrudRepository<GuessAcronyms, Long> {

    Optional<GuessAcronyms> findByAcronym(String acronym);
}
