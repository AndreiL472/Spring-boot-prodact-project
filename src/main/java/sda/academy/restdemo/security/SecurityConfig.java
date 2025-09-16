package sda.academy.restdemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration

public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests( auth-> auth.
                requestMatchers("/home").permitAll()
                        .requestMatchers("/products/add").authenticated()
                        .anyRequest().authenticated()
                ).formLogin(form->form.
                                defaultSuccessUrl("/home",true))
                .logout(logout -> logout.logoutSuccessUrl("/home")
                        .permitAll()
                );
                return http.build();
    }
}
