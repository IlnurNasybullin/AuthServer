package org.freestudy.authserver.service;

import org.freestudy.authserver.model.AuthUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BaseAuthUserService {
    Optional<AuthUser> getUser(String login);

    List<AuthUser> getUsers();
    AuthUser saveUser(AuthUser user);
    AuthUser updateUser(AuthUser user);
}
