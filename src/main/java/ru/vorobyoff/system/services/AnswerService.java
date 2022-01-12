package ru.vorobyoff.system.services;

import ru.vorobyoff.system.domain.Answer;
import ru.vorobyoff.system.rest.bodies.PollAnswers;

import java.util.List;

public interface AnswerService {

    void passPollWithAnswers(Long pollId, PollAnswers answers);

    List<Answer> getUserAnswers(String userId);
}