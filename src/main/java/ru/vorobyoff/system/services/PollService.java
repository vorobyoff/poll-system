package ru.vorobyoff.system.services;

import ru.vorobyoff.system.domain.Poll;
import ru.vorobyoff.system.rest.bodies.PollCreate;
import ru.vorobyoff.system.rest.bodies.PollUpdate;

import java.util.List;

public interface PollService {

    Poll createPoll(PollCreate create);

    List<Poll> findAllPolls();

    Poll update(Long pollId, PollUpdate update);

    void deletePollById(Long pollId);

    Poll findPollById(Long id);
}