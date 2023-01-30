package com.example.springboot3digest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        http
                .authorizeHttpRequests(request -> {
                    request.anyRequest().authenticated();
                })
                .exceptionHandling(e -> e.authenticationEntryPoint(entryPoint()))
                .addFilterBefore(digestAuthenticationFilter(userDetailsService), BasicAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password("user")
                .roles("USER").build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    DigestAuthenticationEntryPoint entryPoint() {
        DigestAuthenticationEntryPoint result = new DigestAuthenticationEntryPoint();
        result.setRealmName("My App Realm");
        result.setKey("3028472b-da34-4501-bfd8-a355c42bdf92");
        return result;
    }

    DigestAuthenticationFilter digestAuthenticationFilter(UserDetailsService userDetailsService) {
        DigestAuthenticationFilter result = new DigestAuthenticationFilter();
        result.setUserDetailsService(userDetailsService);
        result.setAuthenticationEntryPoint(entryPoint());
        return result;
    }
}
