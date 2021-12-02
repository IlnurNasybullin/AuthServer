package org.freestudy.authserver.service;

import lombok.extern.slf4j.Slf4j;
import org.freestudy.authserver.dto.UserDto;
import org.freestudy.authserver.mapper.UserMapper;
import org.freestudy.authserver.model.AuthUser;
import org.freestudy.authserver.model.UserRole;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
@Slf4j
public class UserDtoServiceImpl implements UserDtoService {

    private final BaseUserRoleService userRoleService;
    private final BaseAuthUserService authUserService;
    private final UserMapper mapper;

    public UserDtoServiceImpl(@Qualifier("baseUserRoleServiceImpl") BaseUserRoleService userRoleService,
                              @Qualifier("baseAuthUserServiceImpl") BaseAuthUserService authUserService,
                              UserMapper mapper) {
        this.userRoleService = userRoleService;
        this.authUserService = authUserService;
        this.mapper = mapper;
    }

    @Override
    public UserDto saveUser(UserDto user) {
        Set<UserRole> roles = userRoleService.getRoles(user.getRoles());
        AuthUser authUser = mapper.toUser(user);
        authUser.setRoles(roles);
        AuthUser savedUser = authUserService.saveUser(authUser);
        log.info("Auth user is active: {}", savedUser.isActive());

        return mapper.toUserDto(savedUser);
    }
}
