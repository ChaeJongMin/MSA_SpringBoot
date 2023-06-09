package com.example.usermicroservice.controller;

import com.example.usermicroservice.Service.UserService;
import com.example.usermicroservice.dto.UserDto;
import com.example.usermicroservice.vo.RequestUserVO;
import com.example.usermicroservice.vo.ResponseUserVo;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private final Environment environment;
    private final UserService userService;
    private final Environment env;

    @GetMapping("/health_check")
    @Timed(value = "users.status", longTask = true)
    public String status() {
        return String.format("It's Working in User Service"
                + ", port(local.server.port)=" + env.getProperty("local.server.port")
                + ", port(server.port)=" + env.getProperty("server.port")
                + ", token secret=" + env.getProperty("token.secret")
                + ", token expiration time=" + env.getProperty("token.expiration_time"));
    }

    @GetMapping("/welcome")
    @Timed(value = "users.walcome", longTask = true)
    public String welcome(){
        return environment.getProperty("greeting.message");
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUserVo> createUser(@RequestBody RequestUserVO userVO){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto=modelMapper.map(userVO, UserDto.class);
        userService.createUser(userDto);
        ResponseUserVo responseUserVo=modelMapper.map(userDto,ResponseUserVo.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUserVo);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUserVo>> getUser(){
        List<UserDto> userDtoList=userService.getUserByAll();

        List<ResponseUserVo> userList=new ArrayList<>();
        userDtoList.forEach(v -> {
            userList.add(new ModelMapper().map(v,ResponseUserVo.class));
        });
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }
    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUserVo> getUser(@PathVariable ("userId") String userId){
        UserDto userDto = userService.getUserByUserId(userId);
        log.info("받아온 주문 리스트 크기: "+userDto.getOrderList().size());
        ResponseUserVo responseUserVo=new ModelMapper().map(userDto,ResponseUserVo.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseUserVo);
    }


}
