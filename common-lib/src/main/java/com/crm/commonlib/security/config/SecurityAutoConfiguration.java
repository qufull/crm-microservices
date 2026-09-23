package com.crm.commonlib.security.config;

import com.crm.commonlib.security.exception.JwtAccessDeniedHandler;
import com.crm.commonlib.security.exception.JwtAuthenticationEntryPoint;
import com.crm.commonlib.security.jwt.JwtRoleAuthenticationConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@AutoConfiguration
@Import({
        JwtAuthenticationEntryPoint.class,
        JwtAccessDeniedHandler.class
})
public class SecurityAutoConfiguration {

    @Bean
    public SecurityFilterChain resourceServerSecurityFilterChain(
            HttpSecurity http,
            JwtAuthenticationEntryPoint authenticationEntryPoint,
            JwtAccessDeniedHandler accessDeniedHandler) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtRoleAuthenticationConverter()))
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                );

        return http.build();
    }
}