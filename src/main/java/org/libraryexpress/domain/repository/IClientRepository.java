package org.libraryexpress.domain.repository;

import org.libraryexpress.domain.entity.Client;

import java.util.List;
import java.util.Optional;

public interface IClientRepository {

    Optional<Client> getById(String id);
    Optional<Client> getByEmail(String email);
    Optional<List<Client>> all();
}
