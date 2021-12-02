package org.freestudy.authserver.mapper;

import org.freestudy.authserver.dto.UserDto;
import org.freestudy.authserver.model.AuthUser;
import org.freestudy.authserver.model.UserRole;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    @Qualifier("userPasswordEncoder")
    protected PasswordEncoder encoder;

    @BeforeMapping
    protected void encodePassword(UserDto userDto) {
        if (userDto == null) {
            return;
        }

        String password = userDto.getPassword();
        if (password != null) {
            userDto.setPassword(encoder.encode(password));
        }
        if (userDto.getRoles() == null) {
            userDto.setRoles(Set.of());
        }
    }

    public abstract AuthUser toUser(UserDto userDto);

    protected Set<String> rolesToStrings(Set<UserRole> roles) {
        return roles.stream()
                .map(UserRole::getRoleName)
                .collect(Collectors.toSet());
    }

    protected Set<UserRole> stringsToRoles(Set<String> roles) {
        return roles.stream()
                .map(role -> UserRole.builder().roleName(role).build())
                .collect(Collectors.toSet());
    }

    @Mapping(target="password", expression = "java(null)")
    @Mapping(source = "active", target = "isActive")
    public abstract UserDto toUserDto(AuthUser user);
}
