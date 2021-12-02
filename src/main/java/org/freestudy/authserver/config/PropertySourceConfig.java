package org.freestudy.authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.TimeZone;

@Configuration
@PropertySource("classpath:application.yml")
public class PropertySourceConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(defaultLocale());
        slr.setDefaultTimeZone(defaultTimeZone());
        return slr;
    }

    private TimeZone defaultTimeZone() {
        return TimeZone.getTimeZone("UTC");
    }

    private Locale defaultLocale() {
        return Locale.US;
    }

    @PostConstruct
    public void init() {
        Locale.setDefault(defaultLocale());
        TimeZone.setDefault(defaultTimeZone());
    }
}
