package com.example.usermicroservice.config.Handler;

import com.example.usermicroservice.Service.UserService;
import com.example.usermicroservice.dto.UserDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Date;


@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;
    @Value("${token.expiration_time}")
    private Long EXPIRATION_TIME;
    @Value("${token.secret}")
    private String secretKey;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String userName = ((User)authentication.getPrincipal()).getUsername();
        UserDto userDto =  userService.getUserDetailByEmail(userName);
        log.info("onAuthenticationSuccess 성공");
        String token = Jwts.builder()
                .setSubject(userDto.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() +
                        EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();


        response.addHeader("token",token);
        response.addHeader("userId",userDto.getUserId());
    }
}
