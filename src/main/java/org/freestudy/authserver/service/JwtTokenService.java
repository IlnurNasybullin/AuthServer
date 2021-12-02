package org.freestudy.authserver.service;

import org.freestudy.authserver.dto.JwtTokenDto;
import org.freestudy.authserver.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface JwtTokenService {

    JwtTokenDto generate(UserDto user);
    JwtTokenDto refresh(UserDto user);
    boolean isValid(UserDto user);

}
