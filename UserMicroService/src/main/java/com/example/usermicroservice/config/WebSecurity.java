package com.example.usermicroservice.config;

import com.example.usermicroservice.Service.UserService;
import com.example.usermicroservice.config.Filter.AuthenticationFilter;
import com.example.usermicroservice.config.Handler.CustomSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity{
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;
    private final Environment environment;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        AuthenticationManager authenticationManager = getAuthenticationManager(http);
        http
               .csrf(csrf -> csrf.disable())
                .headers(headers ->
                        headers
                                .frameOptions((frameOptions) -> frameOptions.disable()))
               .authorizeRequests(authorize -> authorize
                               .requestMatchers(PathRequest.toH2Console()).permitAll()
                               .requestMatchers("/actuator/**").permitAll()
                               .requestMatchers("/**").access("hasIpAddress('127.0.0.1') or hasIpAddress('192.168.0.21') or hasIpAddress('172.18.0.0/16')")
                      )
               .addFilter(getAuthenticationFilter());


       return http.build();

    }

//    @Bean
//    public CustomSuccessHandler getCustomSuccessHandler(){
//        return new CustomSuccessHandler(userService);
//    }
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(provider);
    }

    private AuthenticationFilter getAuthenticationFilter() {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService,environment);
        authenticationFilter.setAuthenticationManager(authenticationManager());

        return authenticationFilter;
    }


}
