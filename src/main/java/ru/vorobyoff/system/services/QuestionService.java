package ru.vorobyoff.system.services;

import ru.vorobyoff.system.domain.Poll;
import ru.vorobyoff.system.domain.Question;

public interface QuestionService {

    Poll createNewQuestion(Long pollId, Question question);

    Poll updateQuestion(Long pollId, String questionId, Question question);

    void deleteQuestionById(Long pollId, String questionId);
}