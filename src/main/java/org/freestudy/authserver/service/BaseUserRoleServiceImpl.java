package org.freestudy.authserver.service;

import org.freestudy.authserver.model.UserRole;
import org.freestudy.authserver.repo.UserRoleRepo;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class BaseUserRoleServiceImpl implements BaseUserRoleService {

    private final UserRoleRepo roleRepo;

    public BaseUserRoleServiceImpl(UserRoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserRole> getRole(String roleName) {
        return roleRepo.findByRoleName(roleName);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<UserRole> getRoles(Set<String> roleNames) {
        return roleRepo.findAllByRoleNameIn(roleNames);
    }

    @Override
    public UserRole saveRole(UserRole role) {
        return roleRepo.save(role);
    }

    @Override
    public List<UserRole> saveRoles(List<UserRole> roles) {
        return Streamable.of(roleRepo.saveAll(roles)).toList();
    }
}
