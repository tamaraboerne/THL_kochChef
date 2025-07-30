package sweii.kochchef.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import sweii.kochchef.demo.security.CustomAuthenticationProvider;
import sweii.kochchef.demo.service.UserDetailsServiceImpl;
import sweii.kochchef.demo.service.UserSecurityService;
import sweii.kochchef.demo.service.UserSecurityServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public UserSecurityService userSecurityService() {
        return new UserSecurityServiceImpl();
    }

    @Bean public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider();
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/login", "/registration", "/resources/**", "/css/**", "/js/**", "/images/**").permitAll()
                                .requestMatchers("/main/**").permitAll()
                                .requestMatchers("/static/**").permitAll()
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/load-more/**").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/adminView").hasRole("ADMIN")
                                .requestMatchers("/Startseite/**").permitAll()
                                .requestMatchers("/protected").authenticated() // Geschützte Ressource
                                .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .httpBasic(Customizer.withDefaults()) // Erzwingt die Verwendung von HTTP Basic Authentication
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/main")
                        .invalidateHttpSession(true) // Ungültig machen der Sitzung
                        .deleteCookies("JSESSIONID") // Löschen des JSESSIONID-Cookies
                        .permitAll()
                );


        return http.build();
    }
}