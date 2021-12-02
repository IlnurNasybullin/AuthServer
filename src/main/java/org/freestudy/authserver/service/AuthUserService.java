package org.freestudy.authserver.service;

import org.freestudy.authserver.model.AuthUser;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface AuthUserService extends BaseAuthUserService {

    AuthUser addRoleToUser(AuthUser user, String roleName);
    AuthUser addRolesToUser(AuthUser user, Set<String> roleNames);

    AuthUser setActiveToUser(String login, boolean isActive);
}
