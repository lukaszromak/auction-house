package com.lukasz.auctionhouse.security;

import com.lukasz.auctionhouse.configuration.ProfileNames;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurityConfig{

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Profile(ProfileNames.USERS_IN_MEMORY)
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder) {

        var manager = new InMemoryUserDetailsManager();
        User.UserBuilder userBuilder = User.builder();

        var user = userBuilder
                .username("username")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();

        manager.createUser(user);

        var admin = userBuilder
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .roles("ADMIN")
                .build();

        manager.createUser(admin);

        var superUser = userBuilder
                .username("superuser")
                .password(passwordEncoder.encode("password"))
                .roles("USER", "ADMIN")
                .build();

        manager.createUser(superUser);

        return manager;
    }
}
