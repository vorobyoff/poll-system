package ru.vorobyoff.system.repositories;

import ru.vorobyoff.system.domain.Answer;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository {

    Answer save(Answer answer);

    Optional<Answer> findById(Long id);

    List<Answer> findAllUserAnswers(String userId);
}