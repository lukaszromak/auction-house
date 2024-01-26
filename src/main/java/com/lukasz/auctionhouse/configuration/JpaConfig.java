package com.lukasz.auctionhouse.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig{

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth!=null && auth.isAuthenticated()) {
                var login = ((User)auth.getPrincipal()).getUsername();
                return Optional.of(login);
            }
            return Optional.empty();
        };
    }
}

