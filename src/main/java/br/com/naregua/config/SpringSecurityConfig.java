package br.com.naregua.config;

import br.com.naregua.jwt.JwtAutorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SpringSecurityConfig {

    // 🔹 Constantes do Swagger (sem criar outra classe)
    private static final String SWAGGER_UI = "/swagger-ui/**";
    private static final String SWAGGER_HTML = "/swagger-ui.html";
    private static final String SWAGGER_DOCS = "/v3/api-docs/**";

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtAutorizationFilter jwtAutorizationFilter
    ) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth
                        // 🔓 ROTAS PÚBLICAS
                        .requestMatchers(HttpMethod.POST, "/api/v1/user").permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/auth",
                                "/api/v1/auth/refresh"
                        ).permitAll()

                        // 🔓 SWAGGER LIBERADO
                        .requestMatchers(
                                SWAGGER_UI,
                                SWAGGER_HTML,
                                SWAGGER_DOCS
                        ).permitAll()

                        // 🔒 QUALQUER OUTRA
                        .anyRequest().authenticated()
                )

                // ⚠️ IMPORTANTE: filtro JWT depois das permissões
                .addFilterBefore(
                        jwtAutorizationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
