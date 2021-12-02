package org.freestudy.authserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Configuration
public class AuthenticationConfig {

    @Bean
    public PasswordEncoder authenticationEncoder(@Value("${Authentication.algorithm}") String algorithm,
                                                 @Value("${Authentication.strength}") int strength,
                                                 @Value("${Authentication.seed}") long seed) throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance(algorithm);
        random.setSeed(seed);

        return new BCryptPasswordEncoder(strength, random);
    }

}
