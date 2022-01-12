package ru.vorobyoff.system.repositories;

import ru.vorobyoff.system.domain.Poll;

import java.util.Optional;

public interface PollRepository {

    Optional<Poll> findById(Long id);

    Iterable<Poll> findAll();

    Poll save(Poll poll);

    void deleteById(Long id);
}