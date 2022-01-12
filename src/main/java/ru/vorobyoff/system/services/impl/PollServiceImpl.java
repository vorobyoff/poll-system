package ru.vorobyoff.system.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ru.vorobyoff.system.domain.Poll;
import ru.vorobyoff.system.domain.Question;
import ru.vorobyoff.system.exceptions.EntityNotFoundException;
import ru.vorobyoff.system.exceptions.InvalidDateIntervalException;
import ru.vorobyoff.system.repositories.PollRepository;
import ru.vorobyoff.system.rest.bodies.PollCreate;
import ru.vorobyoff.system.rest.bodies.PollUpdate;
import ru.vorobyoff.system.services.PollService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.springframework.data.util.StreamUtils.createStreamFromIterator;

@Service
@RequiredArgsConstructor
public final class PollServiceImpl implements PollService {

    private final PollRepository pollRepository;
    private final Tx tx;

    @Override
    public Poll createPoll(PollCreate create) {
        requireChronologicalSequence(create.getStartAt(), create.getEndAt());

        final var questions = create.getQuestions().stream()
                .map(it -> Pair.of(generateId(), it))
                .collect(() -> new HashMap<String, Question>(),
                        (acc, p) -> acc.put(p.getFirst(), p.getSecond()),
                        HashMap::putAll);

        final var build = Poll.builder()
                .description(create.getDescription())
                .startAt(create.getStartAt())
                .endAt(create.getEndAt())
                .title(create.getTitle())
                .questions(questions)
                .build();

        return tx.exec(() -> pollRepository.save(build));
    }


    @Override
    public List<Poll> findAllPolls() {
        final var iterable = pollRepository.findAll();
        return createStreamFromIterator(iterable.iterator())
                .collect(toUnmodifiableList());
    }

    @Override
    public Poll update(Long pollId, PollUpdate update) {

        final var existedPoll = pollRepository.findById(pollId)
                .orElseThrow(() -> new EntityNotFoundException(pollId));

        final var updated = Poll.builder()
                .questions(existedPoll.getQuestions())
                .description(update.getDescription())
                .startAt(existedPoll.getStartAt())
                .endAt(update.getEndAt())
                .title(update.getTitle())
                .id(existedPoll.getId())
                .build();

        return tx.exec(() -> {
            if (pollRepository.findById(pollId).isEmpty())
                return null;

            return pollRepository.save(updated);
        });
    }

    @Override
    public void deletePollById(Long pollId) {
        tx.run(() -> pollRepository.deleteById(pollId));
    }

    @Override
    public Poll findPollById(Long id) {
        return pollRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    private String generateId() {
        return randomUUID().toString().replace("-", "");
    }

    private void requireChronologicalSequence(LocalDateTime past, LocalDateTime future) {
        if (isNull(past) || isNull(future)) return;
        if (future.isBefore(past))
            throw new InvalidDateIntervalException(past, future);
    }
}