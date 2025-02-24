package com.locadora.locadoraLivro.Users.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/forgot").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/reset-password").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/reset-password/validate").permitAll()

                        .requestMatchers(HttpMethod.POST, "/user").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/publisher").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/renter").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/book").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/rent").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/user/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/publisher/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/renter/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/book/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/rent/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/user/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/publisher/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/renter/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/book/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/rent/{id}").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .cors(withDefaults())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
