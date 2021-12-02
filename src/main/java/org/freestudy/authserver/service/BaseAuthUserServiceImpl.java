package org.freestudy.authserver.service;

import org.freestudy.authserver.model.AuthUser;
import org.freestudy.authserver.repo.AuthUserRepo;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BaseAuthUserServiceImpl implements BaseAuthUserService {

    private final AuthUserRepo userRepo;

    public BaseAuthUserServiceImpl(AuthUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuthUser> getUser(String login) {
        return userRepo.findFirstByLogin(login);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthUser> getUsers() {
        return Streamable.of(userRepo.findAll()).toList();
    }

    @Override
    public AuthUser saveUser(AuthUser user) {
        user.setId(null);
        return userRepo.save(user);
    }

    @Override
    public AuthUser updateUser(AuthUser user) {
        return userRepo.save(user);
    }
}
