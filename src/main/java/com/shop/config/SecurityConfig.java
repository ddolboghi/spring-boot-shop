package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // Here we are configuring our login form
                .formLogin(formLogin -> {
                            formLogin
                                    .loginPage("/members/login") // Login page will be accessed through this endpoint. We will create a controller method for this.
                                    .usernameParameter("email")
                                    .passwordParameter("password")
                                    .permitAll() // We re permitting all for login page
                                    .defaultSuccessUrl("/") // If the login is successful, user will be redirected to this URL.
                                    .failureUrl("/members/login/error"); // If the user fails to login, application will redirect the user to this endpoint
                        }
                )
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/img/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/members/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/item/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
                            .anyRequest().authenticated();
                    }
                )
                .logout(logout ->
                        logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                                .logoutSuccessUrl("/")
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}