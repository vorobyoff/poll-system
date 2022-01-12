package ru.vorobyoff.system.repositories.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.vorobyoff.system.domain.Answer;
import ru.vorobyoff.system.domain.Poll;
import ru.vorobyoff.system.repositories.AnswerRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

@Repository
@RequiredArgsConstructor
public class AnswerRepositoryImpl implements AnswerRepository {

    private static final String INSERT_ANSWER = "INSERT INTO answer(poll_id, user_id, answer) VALUES (?, ?, (?)::jsonb);";
    private static final String SELECT_ANSWER_WHERE_ID = "SELECT (a.id, a.poll_id, a.user_id, a.answer::text, " +
            "p.title, p.start_at, p.end_at, p.description, p.questions::text) " +
            "FROM answer a JOIN poll p ON p.id = a.poll_id" +
            "WHERE id = ?";
    private static final String SELECT_ANSWERS_WHERE_USER_ID = "SELECT * FROM answer a " +
            "JOIN poll p ON p.id = a.poll_id " +
            "WHERE a.user_id = ?;";

    private final JdbcTemplate template;
    private final ObjectMapper mapper;

    @Override
    public Answer save(Answer answer) {
        final var keyHolder = new GeneratedKeyHolder();

        template.update(it -> {
            final var ps = it.prepareStatement(INSERT_ANSWER, RETURN_GENERATED_KEYS);
            ps.setString(3, mapToJson(answer.getAnswers()));
            ps.setLong(1, answer.getPoll().getId());
            ps.setString(2, answer.getUserId());

            return ps;
        }, keyHolder);
        final var generatedId = (Long) requireNonNull(keyHolder.getKeys()).get("id");

        return answer.setId(generatedId);
    }

    @Override
    public Optional<Answer> findById(Long id) {
        final var found = template.queryForObject(SELECT_ANSWER_WHERE_ID,
                (rs, n) -> rs.next() ? buildAnswer(rs) : null,
                id);

        return ofNullable(found);
    }

    @Override
    public List<Answer> findAllUserAnswers(String userId) {
        return template.query(it -> {
            final var s = it.prepareStatement(SELECT_ANSWERS_WHERE_USER_ID);
            s.setString(1, userId);
            return s;
        }, (rs, n) -> buildAnswer(rs));
    }

    private Answer buildAnswer(ResultSet rs) throws SQLException {
        final var answers = rs.getString("answer");
        final var userId = rs.getString("user_id");
        final var answerId = rs.getLong("id");
        final var poll = buildPoll(rs);

        return new Answer(answerId, poll, userId, mapTo(answers, new TypeReference<>() {
        }));
    }

    private Poll buildPoll(ResultSet rs) throws SQLException {
        final var description = rs.getString("description");
        final var startAt = rs.getTimestamp("start_at");
        final var questions = rs.getString("questions");
        final var endAt = rs.getTimestamp("end_at");
        final var title = rs.getString("title");
        final var id = rs.getLong("poll_id");

        return Poll.builder()
                .questions(mapTo(questions, new TypeReference<>() {
                }))
                .startAt(startAt.toLocalDateTime())
                .endAt(toLocal(endAt))
                .description(description)
                .title(title)
                .id(id)
                .build();
    }

    private LocalDateTime toLocal(Timestamp timestamp) {
        if (isNull(timestamp)) return null;
        return timestamp.toLocalDateTime();
    }

    private <T> T mapTo(String json, TypeReference<T> typeRef) {
        try {
            return mapper.readValue(json, typeRef);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error occurred during json deserializing.", e);
        }
    }

    private String mapToJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error occurred during json serializing.", e);
        }
    }
}