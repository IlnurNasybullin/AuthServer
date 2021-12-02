package org.freestudy.authserver.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = {"accessToken"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenDto {
    private String accessToken;
    private String refreshToken;
}
