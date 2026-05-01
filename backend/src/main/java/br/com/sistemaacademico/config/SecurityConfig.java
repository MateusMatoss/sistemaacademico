package br.com.sistemaacademico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers("/dashboard/**").permitAll() //DEPOIS VOLTAR PARA: .requestMatchers("/dashboard/**").hasRole("ADMIN")
                        .requestMatchers("/logs-acesso/**").hasRole("ADMIN")
                        .requestMatchers("/usuarios/**").hasRole("ADMIN")
                        .requestMatchers("/alunos/**").hasAnyRole("ADMIN", "PROFESSOR", "ALUNO")
                        .requestMatchers("/professores/**").hasAnyRole("ADMIN", "PROFESSOR")
                        .requestMatchers("/cursos/**").hasAnyRole("ADMIN", "PROFESSOR")
                        .requestMatchers("/disciplinas/**").hasAnyRole("ADMIN", "PROFESSOR")
                        .requestMatchers("/matriculas/**").hasRole("ADMIN")
                        .requestMatchers("/notas/**").hasAnyRole("ADMIN", "PROFESSOR")
                        .requestMatchers("/frequencias/**").hasAnyRole("ADMIN", "PROFESSOR")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}