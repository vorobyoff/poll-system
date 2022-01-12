package ru.vorobyoff.system.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vorobyoff.system.domain.Answer;
import ru.vorobyoff.system.domain.Question;
import ru.vorobyoff.system.repositories.AnswerRepository;
import ru.vorobyoff.system.repositories.UserIdRepository;
import ru.vorobyoff.system.rest.bodies.PollAnswers;
import ru.vorobyoff.system.services.AnswerService;
import ru.vorobyoff.system.services.PollService;

import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;
import static ru.vorobyoff.system.domain.Question.Type.*;

@Service
@RequiredArgsConstructor
public final class AnswerServiceImpl implements AnswerService {

    private final PollService pollService;
    private final AnswerRepository answerRepository;
    private final UserIdRepository userIdRepository;
    private final Tx tx;

    @Override
    public void passPollWithAnswers(Long pollId, PollAnswers answers) {
        final var user = createUserIfNotExistsWithGivenId(answers.getUserId());
        final var poll = pollService.findPollById(pollId);
        matchAnswersWithQuestions(answers.getAnswers(), poll.getQuestions());
        answerRepository.save(Answer.of(poll, user, answers.getAnswers()));
    }

    @Override
    public List<Answer> getUserAnswers(String userId) {
        return answerRepository.findAllUserAnswers(userId);
    }

    private String createUserIfNotExistsWithGivenId(String userId) {
        return tx.exec(() -> userIdRepository.find(userId)
                .orElseGet(() -> userIdRepository.save(userId)));
    }

    private void matchAnswersWithQuestions(Map<String, String> answers, Map<String, Question> questions) {
        questions.forEach((key, value) -> {
            final var answer = answers.get(key);
            final var matched = matchAnswer(answer, value);
            if (!matched) {
                throw new IllegalArgumentException("Bad answer!");
            }
        });
    }

    private boolean matchAnswer(String answer, Question question) {
        if (isNull(answer)) return false;
        if (question.getType() == TEXT) return true;

        final var preparedQuestionAnswers = question.getAnswers();

        if (question.getType() == SINGLE)
            return preparedQuestionAnswers.contains(answer);

        if (question.getType() == MULTIPLE) {
            final var multipleAnswers = answer.split("\\|");
            for (var a : multipleAnswers) {
                if (preparedQuestionAnswers.contains(a))
                    return true;
            }
        }

        return false;
    }
}