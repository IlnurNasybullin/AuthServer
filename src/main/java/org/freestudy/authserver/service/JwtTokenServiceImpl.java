package org.freestudy.authserver.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.freestudy.authserver.dto.JwtTokenDto;
import org.freestudy.authserver.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    private final long accessTokenExpires;

    private final long refreshTokenExpires;

    private final Algorithm jwtAlgorithm;

    public JwtTokenServiceImpl(@Value("${jwt.access-token-expires}") long accessTokenExpires,
                               @Value("${jwt.refresh-token-expires}") long refreshTokenExpires,
                               @Value("${jwt.key}") String key) {
        this.accessTokenExpires = accessTokenExpires;
        this.refreshTokenExpires = refreshTokenExpires;
        this.jwtAlgorithm = Algorithm.HMAC512(key.getBytes(StandardCharsets.UTF_8));
    }

    private String accessToken(String login, List<String> roles, String uri) {
        return JWT.create()
                .withSubject(login)
                .withExpiresAt(Date.from(Instant.now().plus(accessTokenExpires, ChronoUnit.SECONDS)))
                .withIssuer(uri)
                .withClaim("roles", roles)
                .sign(jwtAlgorithm);
    }

    private String refreshToken(String login, String uri) {
        return JWT.create()
                .withSubject(login)
                .withExpiresAt(Date.from(Instant.now().plus(refreshTokenExpires, ChronoUnit.SECONDS)))
                .withIssuer(uri)
                .sign(jwtAlgorithm);
    }

    @Override
    public JwtTokenDto generate(UserDto user) {
        String login = user.getLogin();
        List<String> roles = new ArrayList<>(user.getRoles());
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();

        return JwtTokenDto.builder()
                .accessToken(accessToken(login, roles, uri))
                .refreshToken(refreshToken(login, uri))
                .build();
    }

    @Override
    public JwtTokenDto refresh(UserDto user) {
        return null;
    }

    @Override
    public boolean isValid(UserDto user) {
        return false;
    }
}
