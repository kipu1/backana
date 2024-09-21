package com.example.Nutriologa.Analia.Roman.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, UserDetailsService userDetailsService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // Asegurarse de que CORS está activado
                .csrf(csrf -> csrf.disable())  // Deshabilitar CSRF
                .authorizeHttpRequests(auth -> auth
                        // Permitir todas las solicitudes OPTIONS para preinspección (CORS)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // Asegúrate de permitir las preflight requests
                        // Rutas abiertas al público
                        .requestMatchers(
                                "/api/usuarios/login",
                                "/api/citas/listar",
                                "/api/cursos/**",
                                "/api/citas/agendar",
                                "/api/servicios/listar",
                                "/api/servicios/listar/{id}",
                                "/api/cursos/listar",
                                "/api/cursos/{filename:.+}",
                                "/api/cursos/descargar/{id}",
                                "/api/cursos/descargar/**",
                                "/api/usuarios/registro",
                                "/archivos/**"  // Permitir acceso público a los archivos PDF
                        ).permitAll()
                        // Rutas que requieren autenticación
                        .requestMatchers("/api/cursos/subir-archivo", "/api/usuarios/perfil",
                                "/api/citas/historia",  "/api/cursos/crear", "/api/citas/obtener",
                                "/api/cursos/actualizar/**", "/api/cursos/actualizar/{id}",
                                "/api/servicios/actualizar/{id}", "/api/servicios/actualizar/**")
                        .authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Política sin estado para JWT
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)  // Añadir filtro de JWT
                .build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://analiaromannutricionista.netlify.app"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization"));  // Permitir que el frontend acceda a ciertos encabezados en la respuesta
        configuration.setAllowCredentials(true);  // Permitir el envío de credenciales como tokens
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }
}