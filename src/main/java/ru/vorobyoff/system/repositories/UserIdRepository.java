package ru.vorobyoff.system.repositories;

import java.util.Optional;

public interface UserIdRepository {

    Optional<String> find(String id);

    String save(String id);
}