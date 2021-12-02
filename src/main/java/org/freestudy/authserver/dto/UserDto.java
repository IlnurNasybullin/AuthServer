package org.freestudy.authserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.freestudy.authserver.validator.Login;
import org.freestudy.authserver.validator.groups.Registration;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = {"id", "login"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto implements Resource {

    private Long id;

    @Login(groups = {Registration.class})
    private String login;
    private String password;
    private JwtTokenDto token;
    private Set<String> roles;
    private Boolean isActive;

}
