package org.freestudy.authserver.filter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Component
@Order(1)
public class AuthenticationFilter implements Filter {

    private final String hashedSecretKey;
    private final PasswordEncoder encoder;

    public AuthenticationFilter(@Qualifier("authenticationEncoder") PasswordEncoder encoder,
                                @Value("${Authentication.key}") String secretKey) {
        this.encoder = encoder;
        this.hashedSecretKey = secretKey;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestKey = ((HttpServletRequest) request).getHeader("Authorization");
        String bearerToken = "";
        if (requestKey != null && requestKey.startsWith("Bearer")) {
            bearerToken = requestKey.substring(7);
        }

        if (encoder.matches(hashedSecretKey, bearerToken)) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
