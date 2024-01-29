package com.lukasz.auctionhouse.security;


import com.lukasz.auctionhouse.configuration.SpaWebFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class CustomWebSecurityConfigurerAdapter {
    public static final String contextPath = "/api";
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.POST, contextPath + "/items").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, contextPath + "/items/**").permitAll()
                        .requestMatchers(HttpMethod.GET, contextPath + "/itemCategories").permitAll()
                        .requestMatchers(HttpMethod.POST, contextPath + "/itemCategories").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, contextPath + "/itemCategories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, contextPath + "/itemCategories").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, contextPath + "/itemProducers").permitAll()
                        .requestMatchers(HttpMethod.POST, contextPath + "/itemProducers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, contextPath + "/itemProducers/{producerId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, contextPath + "/itemProducers").hasRole("ADMIN")
                        .requestMatchers(contextPath + "/auth/**").permitAll()
                        .requestMatchers(contextPath + "/error").permitAll()
                        .requestMatchers(contextPath + "/bids/{itemId}").permitAll()
                        .requestMatchers(contextPath + "/bids/placeBid").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/", "/index.html", "/static/**",
                                "/*.ico", "/*.json", "/*.png", "/api/user").permitAll()
                        .anyRequest().authenticated()
                );
        http.headers().frameOptions().disable();
        http.cors(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class);
        return http.build();
    }

}
