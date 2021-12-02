package org.freestudy.authserver.repo;

import org.freestudy.authserver.model.UserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface UserRoleRepo extends CrudRepository<UserRole, Long> {

    Optional<UserRole> findByRoleName(String roleName);
    Set<UserRole> findAllByRoleNameIn(Set<String> roleNames);

}
