package org.freestudy.authserver.service;

import org.freestudy.authserver.model.AuthUser;
import org.freestudy.authserver.model.UserRole;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class AuthUserServiceImpl implements AuthUserService {

    private final BaseAuthUserService baseService;
    private final BaseUserRoleService baseRoleService;

    public AuthUserServiceImpl(@Qualifier("baseAuthUserServiceImpl") BaseAuthUserService baseService,
                               @Qualifier("baseUserRoleServiceImpl") BaseUserRoleService baseRoleService) {
        this.baseService = baseService;
        this.baseRoleService = baseRoleService;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuthUser> getUser(String login) {
        return baseService.getUser(login);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthUser> getUsers() {
        return baseService.getUsers();
    }

    @Override
    public AuthUser saveUser(AuthUser user) {
        return baseService.saveUser(user);
    }

    @Override
    public AuthUser updateUser(AuthUser user) {
        return baseService.updateUser(user);
    }

    @Override
    public AuthUser addRoleToUser(AuthUser user, String roleName) {
        UserRole role = baseRoleService.getRole(roleName).orElseThrow();

        user.getRoles().add(role);
        return baseService.updateUser(user);
    }

    @Override
    public AuthUser addRolesToUser(AuthUser user, Set<String> roleNames) {
        Set<UserRole> roles = baseRoleService.getRoles(roleNames);

        user.getRoles().addAll(roles);

        return baseService.updateUser(user);
    }

    @Override
    public AuthUser setActiveToUser(String login, boolean isActive) {
        AuthUser user = baseService.getUser(login).orElseThrow();
        user.setActive(isActive);

        return baseService.updateUser(user);
    }
}
