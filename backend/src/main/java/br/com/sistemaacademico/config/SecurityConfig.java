package br.com.sistemaacademico.config;

import org.springframework.http.HttpMethod;
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
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/registrar", "/auth/cursos").permitAll()
                        .requestMatchers("/auth/primeiro-acesso").authenticated()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()

                        .requestMatchers("/dashboard/**").hasAnyRole("ADMIN", "PROFESSOR")
                        .requestMatchers("/logs-acesso/**").hasRole("ADMIN")
                        .requestMatchers("/usuarios/**").hasRole("ADMIN")
                        .requestMatchers("/alunos/me/**").hasRole("ALUNO")
                        .requestMatchers(HttpMethod.GET, "/alunos/**").hasAnyRole("ADMIN", "PROFESSOR", "ALUNO")
                        .requestMatchers(HttpMethod.POST, "/alunos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/alunos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/alunos/**").hasRole("ADMIN")
                        .requestMatchers("/professores/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/cursos/**").hasAnyRole("ADMIN", "PROFESSOR")
                        .requestMatchers(HttpMethod.POST, "/cursos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/cursos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/cursos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/disciplinas/**").hasAnyRole("ADMIN", "PROFESSOR")
                        .requestMatchers(HttpMethod.POST, "/disciplinas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/disciplinas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/disciplinas/**").hasRole("ADMIN")
                        .requestMatchers("/matriculas/disciplina/**").hasAnyRole("ADMIN", "PROFESSOR")
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
