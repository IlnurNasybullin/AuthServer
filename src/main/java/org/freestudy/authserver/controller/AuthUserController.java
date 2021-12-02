package org.freestudy.authserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.freestudy.authserver.dto.JwtTokenDto;
import org.freestudy.authserver.dto.UserDto;
import org.freestudy.authserver.mapper.UserMapper;
import org.freestudy.authserver.model.AuthUser;
import org.freestudy.authserver.service.AuthUserService;
import org.freestudy.authserver.service.JwtTokenService;
import org.freestudy.authserver.service.UserDtoService;
import org.freestudy.authserver.validator.groups.Registration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthUserController {

    private final UserDtoService userService;
    private final JwtTokenService jwtService;
    private final UserMapper mapper;

    public AuthUserController(@Qualifier("userDtoServiceImpl") UserDtoService userService,
                              @Qualifier("jwtTokenServiceImpl") JwtTokenService jwtService,
                              UserMapper mapper) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.mapper = mapper;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Validated(Registration.class) UserDto userDto) {
        userDto.setIsActive(null);
        UserDto savedUser = userService.saveUser(userDto);
        log.info("User is active: {}", userDto.getIsActive());

        JwtTokenDto token = jwtService.generate(savedUser);
        savedUser.setToken(token);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .build()
                .toUri();

        return ResponseEntity.created(uri).body(savedUser);
    }

}
