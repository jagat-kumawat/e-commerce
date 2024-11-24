package com.ecommerce.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    private JWTRequestFilter jwtRequestFilter;

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/auth/me","/auth/reset","/auth/forgot",
                       "/product/**","/user/**","/websocket","/websocket/**").permitAll()
                        .requestMatchers("/error").permitAll()// For Springdoc
                      .anyRequest().authenticated())
    .build();
}
//@Bean
//    public PasswordEncoder bCryptPasswordEncoder(){
//       return new BCryptPasswordEncoder();
//}

}
