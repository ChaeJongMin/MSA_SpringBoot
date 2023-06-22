package com.example.usermicroservice.config.Filter;


import com.example.usermicroservice.Service.UserService;
import com.example.usermicroservice.dto.UserDto;
import com.example.usermicroservice.vo.RequestLoginVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final UserService userService;
    private final Environment environment;
    private Key key;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(environment.getProperty("token.secret"));
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("attemptAuthentication");
        try{
            RequestLoginVo cred=new ObjectMapper().readValue(request.getInputStream(), RequestLoginVo.class);
            log.info(cred.getEmail()+" "+cred.getPassword());
            return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                    cred.getEmail(),
                    cred.getPassword(),
                    new ArrayList<>()
            ));
        } catch(IOException e){
            log.info("attemptAuthentication에서 오류 발생");
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("successfulAuthentication");
        String userName = ((User)authResult.getPrincipal()).getUsername();
        UserDto userDto =  userService.getUserDetailByEmail(userName);

        byte[] keyBytes = Decoders.BASE64.decode(environment.getProperty("token.secret"));
        this.key = Keys.hmacShaKeyFor(keyBytes);

        String token = Jwts.builder()
                .setSubject(userDto.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() +
                        Long.parseLong(environment.getProperty("token.expiration_time"))))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        response.addHeader("token",token);
        response.addHeader("userId",userDto.getUserId());

    }
}
