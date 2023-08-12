package com.project.bkcollection.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.bkcollection.api.handlers.TokenAccessRequestFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	
	@Order(1)
    @Configuration
    public class ApiSecurityConfig {
    	
        @Autowired
        private TokenAccessRequestFilter accessTokenRequestFilter;
        @Autowired
        private AuthenticationEntryPoint authenticationEntryPoint;
        @Autowired
        private AccessDeniedHandler accessDeniedHandler;
       
		@Bean
		protected SecurityFilterChain ApiHttpSecurity(HttpSecurity http) throws Exception {

            http.authorizeHttpRequests(authorizeRequests ->
                    authorizeRequests
                            .requestMatchers("/api/**", "/auth/**", "/swagger-ui/**")
            )
            .authorizeHttpRequests(authorizeRequests ->
                    authorizeRequests
                            .anyRequest()
                            .permitAll()
            )
            .csrf(csrfCustomizer ->
                    csrfCustomizer
                            .disable()
            )
            .sessionManagement(sessionManagementCustomizer ->
                    sessionManagementCustomizer
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(accessTokenRequestFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exceptionHandlingCustomizer ->
                    exceptionHandlingCustomizer
                            .authenticationEntryPoint(authenticationEntryPoint)
                            .accessDeniedHandler(accessDeniedHandler)
            )
            .cors(
            	withDefaults()
            );
			
			return http.build();
		}
		
		@Bean
		protected AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
			return auth.getAuthenticationManager();
		}
	}
}
