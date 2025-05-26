package com.adri.api_contable_360.security;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Autowired
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    //Este bean va a encargarse de verificar la información de los usuarios que se loguearán en nuestra api
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //Con este bean nos encargaremos de encriptar todas nuestras contraseñas
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Este bean incorporará el filtro de seguridad de json web token que creamos en nuestra clase anterior
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }



    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .exceptionHandling() //Permitimos el manejo de excepciones
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) //Nos establece un punto de entrada personalizado de autenticación para el manejo de autenticaciones no autorizadas
                .and()
                .sessionManagement() //Permite la gestión de sessiones
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET,"/api/obligaciones").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/obligaciones/upload").permitAll()// Habilita esta ruta para todosd
                .requestMatchers(HttpMethod.GET,"/api/obligaciones/{idObligacion}/vencimientos").permitAll()// Habilita esta ruta para todosd
                .requestMatchers(HttpMethod.POST,"/api/obligaciones").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/obligaciones/{idObligacion}").permitAll()
                .requestMatchers(HttpMethod.PUT,"/api/obligaciones/{id}").permitAll()
                .requestMatchers(HttpMethod.DELETE,"/api/obligaciones/{id}").permitAll()



                /// Login

                .requestMatchers(HttpMethod.POST,"/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/auth/userLogueado").permitAll()




                /// Vencimientos

                .requestMatchers(HttpMethod.GET,"/api/vencimientos/{idVencimiento}").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/vencimientos").permitAll()


                // Asignaciones
                .requestMatchers(HttpMethod.GET,"/api/asignaciones").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/asignaciones").permitAll()


                .requestMatchers("/api/asignaciones/{idAsignacion}/vencimientos").permitAll()
                .requestMatchers("/api/asignaciones/{idAsignacion}").permitAll()


                // Asignaciones de clientes
                .requestMatchers(HttpMethod.GET,"/api/asignaciones/cliente/{idCliente}").permitAll()



                // Clientes

                .requestMatchers("/api/clientes").permitAll()
                .requestMatchers("/api/clientes/{idCliente}").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/clientes/asignar/{idCliente}/usuario/{idUsuario}").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/clientes/usuario/{idUsuario}").permitAll()

                // Contactos

                .requestMatchers(HttpMethod.GET,"/api/contactos").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/contactos/{id}").permitAll()
                // Contactos por cliente
                .requestMatchers(HttpMethod.GET,"/api/contactos/cliente/{idCliente}").permitAll()

                .requestMatchers(HttpMethod.POST,"/api/contactos/clientes/{idCliente}").permitAll()
                .requestMatchers(HttpMethod.PUT,"/api/contactos").permitAll()
                .requestMatchers(HttpMethod.DELETE,"/api/contactos/{id}").permitAll()


                // Asignaciones Vencimientos
                .requestMatchers(HttpMethod.GET,"/api/asignaciones-vencimientos").permitAll()

                // Asignaciones Vencimiento por cliente
                .requestMatchers(HttpMethod.GET,"/api/asignaciones-vencimientos/cliente/{idCliente}").permitAll()


                // Proximas a vencer

                .requestMatchers(HttpMethod.GET,"/api/asignaciones-vencimientos/proximas-a-vencer").permitAll()



                .requestMatchers(HttpMethod.GET,"/api/asignaciones-vencimientos/{idAsignacionVencimiento}").permitAll()
                .requestMatchers(HttpMethod.PUT,"/api/asignaciones-vencimientos/{idAsignacionVencimiento}").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/asignaciones-vencimientos/enviar-notificacion").permitAll()

                // Usuarios
                .requestMatchers(HttpMethod.POST,"/api/usuarios").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/usuarios").permitAll()


                .anyRequest().authenticated()
                .and()
                .httpBasic(); // Todas las demás rutas requieren autenticación
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("capacitor://localhost", "https://localhost", "http://localhost:5173","https://teal-figolla-377874.netlify.app")); // Origen permitido List.of("http://localhost:5173","https://frontspa.netlify.app/")
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.addExposedHeader("Authorization"); // Permite exponer el header Authorization

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
