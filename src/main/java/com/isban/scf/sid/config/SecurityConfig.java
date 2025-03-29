package com.isban.scf.sid.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.isban.scf.sid.handler.CustomAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final UserDetailsService userDetailsService;
	
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests(authorizeRequests ->
	            authorizeRequests
	                .antMatchers("/user/login", 
	                			 "/user/create",
	                			 "/user/confirm", 
	                			 "/user/requestUpdatePassword",
	                			 "/user/updatePassword",
	                			 "/user/getAll",
	                			 "/user/deleteAll",
	                			 "/home",
	                			 "/css/**",
	                			 "/img/**",
	                			 "/js/**" 
	                			 ).permitAll() // Permitir acceso sin autenticación
	                .anyRequest().authenticated() // Cualquier otra ruta requiere autenticación
	        )
	        .formLogin(formLogin ->
	            formLogin
	                .loginPage("/user/login") // Página de login personalizada
	                .defaultSuccessUrl("/list", true) // Redirigir al home después del login exitoso
	                .permitAll()
	                .failureHandler(authenticationFailureHandler())
	        )
	        .logout(logout ->
	            logout
	                .logoutUrl("/logout")
	                .logoutSuccessUrl("/user/login?logout") // Redirigir a login después del logout
	                .invalidateHttpSession(true)
	                .deleteCookies("JSESSIONID")
	                .permitAll()
	        )
	        .exceptionHandling(exceptionHandling -> 
	            exceptionHandling.accessDeniedPage("/user/accessDenied")); // Manejo de accesos denegados

	    return http.build();
	}
	    
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }    
    
}