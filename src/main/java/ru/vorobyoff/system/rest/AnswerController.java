package ru.vorobyoff.system.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vorobyoff.system.domain.Answer;
import ru.vorobyoff.system.rest.bodies.PollAnswers;
import ru.vorobyoff.system.services.AnswerService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/polls/{pollId}/answers")
public final class AnswerController {

    private final AnswerService answerService;

    @PostMapping("/pass")
    public void passPoll(@Positive @PathVariable Long pollId,
                         @Validated @RequestBody PollAnswers body) {
        answerService.passPollWithAnswers(pollId, body);
    }

    @GetMapping("/{userId}")
    public List<Answer> getAnswers(@PathVariable(required = false) Long pollId,
                                   @NotBlank @PathVariable String userId) {
        return answerService.getUserAnswers(userId);
    }
}