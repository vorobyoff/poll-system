package ru.vorobyoff.system.repositories.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.vorobyoff.system.domain.Poll;
import ru.vorobyoff.system.repositories.PollRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

@Repository
@RequiredArgsConstructor
public class PollRepositoryImpl implements PollRepository {

    private static final String SELECT_POLL_WHERE_ID = "SELECT p.id, p.title, p.start_at, p.end_at, p.description, p.questions::text FROM poll p WHERE id=?;";
    private static final String INSERT_POLL = "INSERT INTO poll(title, start_at, end_at, description, questions) VALUES (?, ?, ?, ?, (?)::jsonb);";
    private static final String SELECT_ALL_POLLS = "SELECT p.id, p.title, p.start_at, p.end_at, p.description, p.questions::text FROM poll p;";
    private static final String DELETE_POLL_WHERE_ID = "DELETE FROM poll p WHERE p.id = ?;";
    private static final String UPDATE_POLL_WHERE_ID = "UPDATE poll p SET " +
            "title = ?, end_at = ?, description = ?, questions = (?)::jsonb " +
            "WHERE p.id = ?;";

    private final JdbcTemplate template;
    private final ObjectMapper mapper;

    @Override
    public Iterable<Poll> findAll() {
        return template.query(SELECT_ALL_POLLS, (rs, n) -> buildPoll(rs));
    }

    @Override
    public Poll save(Poll poll) {
        return poll.isSaved() ? update(poll) : insert(poll);
    }

    @Override
    public void deleteById(Long id) {
        template.update(DELETE_POLL_WHERE_ID, id);
    }

    @Override
    @NonNull
    public Optional<Poll> findById(Long id) {
        final var found = template.query(SELECT_POLL_WHERE_ID,
                rs -> rs.next() ? buildPoll(rs) : null,
                id);

        return ofNullable(found);
    }

    private Poll update(Poll poll) {
        final var keyHolder = new GeneratedKeyHolder();
        template.update(it -> {
            final var ps = it.prepareStatement(UPDATE_POLL_WHERE_ID, RETURN_GENERATED_KEYS);
            ps.setTimestamp(2, toTimestamp(poll.getEndAt()));
            ps.setString(4, mapToJson(poll.getQuestions()));
            ps.setString(3, poll.getDescription());
            ps.setString(1, poll.getTitle());
            ps.setLong(5, poll.getId());

            return ps;
        }, keyHolder);

        final var savedId = (Long) requireNonNull(keyHolder.getKeys()).get("id");

        return poll.setId(savedId);
    }

    private Poll insert(Poll poll) {
        final var keyHolder = new GeneratedKeyHolder();
        template.update(it -> {
            final var ps = it.prepareStatement(INSERT_POLL, RETURN_GENERATED_KEYS);
            ps.setTimestamp(2, toTimestamp(poll.getStartAt()));
            ps.setTimestamp(3, toTimestamp(poll.getEndAt()));
            ps.setString(5, mapToJson(poll.getQuestions()));
            ps.setString(4, poll.getDescription());
            ps.setString(1, poll.getTitle());

            return ps;
        }, keyHolder);


        final var savedId = (Long) requireNonNull(keyHolder.getKeys()).get("id");

        return poll.setId(savedId);
    }

    private Poll buildPoll(ResultSet rs) throws SQLException {
        final var description = rs.getString("description");
        final var startAt = rs.getTimestamp("start_at");
        final var questions = rs.getString("questions");
        final var endAt = rs.getTimestamp("end_at");
        final var title = rs.getString("title");
        final var id = rs.getLong("id");

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

    private Timestamp toTimestamp(LocalDateTime dateTime) {
        return isNull(dateTime) ? null : Timestamp.valueOf(dateTime);
    }

    private LocalDateTime toLocal(Timestamp timestamp) {
        return isNull(timestamp) ? null : timestamp.toLocalDateTime();
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
            throw new IllegalArgumentException("Error occurred during parsing json from database.", e);
        }
    }
}