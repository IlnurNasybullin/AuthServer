package org.freestudy.authserver.service;

import org.freestudy.authserver.model.UserRole;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public interface BaseUserRoleService {

    Optional<UserRole> getRole(String roleName);
    Set<UserRole> getRoles(Set<String> roleNames);
    UserRole saveRole(UserRole role);
    List<UserRole> saveRoles(List<UserRole> roles);

}
