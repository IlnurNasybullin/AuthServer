package org.freestudy.authserver.service;

import org.freestudy.authserver.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface UserDtoService {
    UserDto saveUser(UserDto user);
}
