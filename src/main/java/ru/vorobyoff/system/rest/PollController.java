package ru.vorobyoff.system.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vorobyoff.system.domain.Poll;
import ru.vorobyoff.system.rest.bodies.PollCreate;
import ru.vorobyoff.system.rest.bodies.PollUpdate;
import ru.vorobyoff.system.services.PollService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/polls")
@RequiredArgsConstructor
public final class PollController {

    private final PollService pollService;

    @PutMapping("/{pollId}/update")
    public Poll updatePoll(@Positive @PathVariable Long pollId,
                           @Validated @RequestBody PollUpdate body) {
        return pollService.update(pollId, body);
    }

    @PostMapping("/new")
    public Poll createPoll(@Validated @RequestBody PollCreate body) {
        return pollService.createPoll(body);
    }

    @DeleteMapping("/{pollId}/delete")
    public void deletePoll(@Positive @PathVariable Long pollId) {
        pollService.deletePollById(pollId);
    }

    @GetMapping("/{pollId}")
    public Poll readPoll(@Positive @PathVariable Long pollId) {
        return pollService.findPollById(pollId);
    }

    @GetMapping
    public List<Poll> readPolls() {
        return pollService.findAllPolls();
    }
}