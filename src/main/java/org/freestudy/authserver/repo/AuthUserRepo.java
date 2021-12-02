package org.freestudy.authserver.repo;

import org.freestudy.authserver.model.AuthUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthUserRepo extends CrudRepository<AuthUser, Long> {
    Optional<AuthUser> findFirstByLogin(String login);
}
