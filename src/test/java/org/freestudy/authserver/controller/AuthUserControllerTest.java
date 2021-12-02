package org.freestudy.authserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.freestudy.authserver.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String hashedKey;

    private final ObjectMapper mapper = new ObjectMapper();

    public AuthUserControllerTest(@Qualifier("authenticationEncoder") PasswordEncoder encoder,
                                  @Value("${Authentication.key}") String secretKey) {
        hashedKey = encoder.encode(secretKey);
    }

    @Test
    public void testMockMvcIsExist() {
        assertThat(mockMvc, notNullValue());
    }

    @Test
    public void testUnauthorizedTryRegister() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register"))
                .andExpect(status().isUnauthorized());
    }

    private MockHttpServletRequestBuilder authorize(MockHttpServletRequestBuilder builder) {
        return builder.header("Authorization", "Bearer " + getHashedKey());
    }

    private String getHashedKey() {
        return hashedKey;
    }

    @ParameterizedTest
    @MethodSource("successfulRegisterUsers")
    @Transactional
    @Rollback
    public void testSuccessfulRegister(UserDto user) throws Exception {
        mockMvc.perform(authorize(MockMvcRequestBuilders.post("/auth/register"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(convertJson(user)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$", allOf(
                    notNullValue(),
                    hasKey("id"),
                    hasKey("login"),
                    hasKey("token"),
                    hasKey("roles"),
                    not(hasKey("password"))
            )))
            .andExpect(jsonPath("$.id", allOf(
                    notNullValue(),
                    anyOf(
                            isA(Integer.class),
                            isA(Long.class)
                    ),
                    greaterThan(0)
            )))
            .andExpect(jsonPath("$.login", equalTo(user.getLogin())))
            .andExpect(jsonPath("$.token", allOf(
                    notNullValue(),
                    hasKey("accessToken"),
                    hasKey("refreshToken")
            )))
            .andExpect(jsonPath("$.token.accessToken", allOf(
                    notNullValue(),
                    isA(String.class)
            )))
            .andExpect(jsonPath("$.token.refreshToken", allOf(
                    notNullValue(),
                    isA(String.class)
            )))
            .andExpect(jsonPath("$.roles", allOf(
                    notNullValue(),
                    hasSize(greaterThanOrEqualTo(0)),
                    everyItem(allOf(
                            notNullValue(),
                            isA(String.class)
                    ))
            )));
    }

    private static Stream<Arguments> successfulRegisterUsers() {
        return Stream.of(
                Arguments.of(UserDto.builder()
                        .login("login00")
                        .password("password0")
                        .roles(Set.of())
                        .build())
        );
    }


    @ParameterizedTest
    @MethodSource("exceptionRegisterUsers")
    @Transactional
    @Rollback
    public void testExceptionRegisterUsers(UserDto user) throws Exception {
        mockMvc.perform(authorize(MockMvcRequestBuilders.post("/auth/register"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertJson(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", allOf(
                        notNullValue(),
                        hasKey("code"),
                        hasKey("fieldErrors")
                )))
                .andExpect(jsonPath("$.code", equalTo(HttpStatus.BAD_REQUEST.value())));
    }

    private static Stream<Arguments> exceptionRegisterUsers() {
        return Stream.of(
                Arguments.of(UserDto.builder()
                        .login(null)
                        .password("password0")
                        .build())
        );
    }

    private String convertJson(Object object) throws JsonProcessingException {
        return mapper.writer().writeValueAsString(object);
    }

}
