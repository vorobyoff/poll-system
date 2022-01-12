package ru.vorobyoff.system.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vorobyoff.system.domain.Poll;
import ru.vorobyoff.system.domain.Question;
import ru.vorobyoff.system.services.QuestionService;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/polls/{pollId}")
@RequiredArgsConstructor
public final class QuestionController {

    private final QuestionService questionService;

    @PutMapping("/update/question/{questionId}")
    public Poll updateQuestion(@Positive @PathVariable Long pollId,
                               @Positive @PathVariable String questionId,
                               @Validated @RequestBody Question body) {
        return questionService.updateQuestion(pollId, questionId, body);
    }

    @PutMapping("/questions/new")
    public Poll createQuestion(@Positive @PathVariable Long pollId,
                               @Validated @RequestBody Question body) {
        return questionService.createNewQuestion(pollId, body);
    }

    @PutMapping("/delete/question/{questionId}")
    public void deleteQuestion(@Positive @PathVariable Long pollId,
                               @Positive @PathVariable String questionId) {
        questionService.deleteQuestionById(pollId, questionId);
    }
}