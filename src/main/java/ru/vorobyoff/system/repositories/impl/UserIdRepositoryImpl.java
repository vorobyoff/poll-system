package ru.vorobyoff.system.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.vorobyoff.system.repositories.UserIdRepository;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@Repository
@RequiredArgsConstructor
public class UserIdRepositoryImpl implements UserIdRepository {

    private static final String SELECT_ID = "SELECT u.id FROM users u WHERE u.id = ?;";
    private static final String INSERT_USER_ID = "INSERT INTO users(id) VALUES (?);";
    private final JdbcTemplate template;

    @Override
    public String save(String id) {
        final var keyHolder = new GeneratedKeyHolder();
        template.update(it -> {
            final var ps = it.prepareStatement(INSERT_USER_ID);
            ps.setString(1, id);
            return ps;
        }, keyHolder);

        return find(id).orElseThrow(IllegalStateException::new);
    }

    @Override
    public Optional<String> find(String id) {
        final var found = template.query(SELECT_ID,
                rs -> rs.next() ? rs.getString("id") : null,
                id);

        return ofNullable(found);
    }
}