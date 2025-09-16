package org.libraryexpress.domain.repository;

import org.libraryexpress.domain.entity.Client;

import java.util.Optional;
import java.util.Set;

public interface IClientRepository {

    void create(Client client);
    boolean update(String id, String email);

    Optional<Client> getById(String id);
    Optional<Client> getByEmail(String email);
    Optional<Set<Client>> all();
}
