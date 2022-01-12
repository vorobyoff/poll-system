package ru.vorobyoff.system.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vorobyoff.system.domain.Poll;
import ru.vorobyoff.system.domain.Question;
import ru.vorobyoff.system.exceptions.EntityNotFoundException;
import ru.vorobyoff.system.repositories.PollRepository;
import ru.vorobyoff.system.services.QuestionService;

import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public final class QuestionServiceImpl implements QuestionService {

    private final PollRepository pollRepository;
    private final Tx tx;

    @Override
    public Poll createNewQuestion(Long pollId, Question question) {
        final var poll = findPoll(pollId);
        poll.getQuestions().put(generateId(), question);
        return savePoll(poll);
    }

    @Override
    public Poll updateQuestion(Long pollId, String questionId, Question update) {
        final var poll = findPoll(pollId);

        final var isUpdated = poll.getQuestions()
                .computeIfPresent(questionId, (k, v) -> Question.builder()
                        .answers(update.getAnswers())
                        .type(update.getType())
                        .text(update.getText())
                        .build()) != null;

        return isUpdated ? savePoll(poll) : null;
    }

    @Override
    public void deleteQuestionById(Long pollId, String questionId) {
        final var poll = findPoll(pollId);
        final var isRemoved = poll.getQuestions().remove(questionId) != null;
        if (isRemoved) savePoll(poll);
    }

    private Poll findPoll(Long pollId) {
        return pollRepository.findById(pollId)
                .orElseThrow(() -> new EntityNotFoundException(pollId));
    }

    private Poll savePoll(Poll poll) {
        return tx.exec(() -> {
            if (pollRepository.findById(poll.getId()).isEmpty()) {
                return null;

            } else return pollRepository.save(poll);
        });
    }

    private String generateId() {
        return randomUUID().toString().replace("-", "");
    }
}