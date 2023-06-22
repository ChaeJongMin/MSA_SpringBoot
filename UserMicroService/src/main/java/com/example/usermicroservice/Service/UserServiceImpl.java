package com.example.usermicroservice.Service;

import com.example.usermicroservice.domain.UserEntity;
import com.example.usermicroservice.domain.UserRepository;
import com.example.usermicroservice.dto.UserDto;
import com.example.usermicroservice.vo.ResponseOrderVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Override
    public UserDto createUser(UserDto userDto) {
        int userIdLen = (int) (Math.random() * 5) + 6;
        String randomUserId= RandomStringUtils.random(userIdLen,true,true);

        userDto.setUserId(randomUserId);

        ModelMapper mapper=new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

        userRepository.save(userEntity);

        UserDto returnUserDto=mapper.map(userEntity,UserDto.class);

        return returnUserDto;

    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserDto userDto;
        try {
            userDto = new ModelMapper().map(userRepository.findByUserId(userId), UserDto.class);
        } catch (NoSuchElementException e) {
            throw new UsernameNotFoundException("해당 유저는 존재하지 않습니다.");
        }
        List<ResponseOrderVo> orderList=new ArrayList<>();
        userDto.setOrderList(orderList);
        return userDto;
    }

    @Override
    public List<UserDto> getUserByAll() {
        List<UserEntity> users = userRepository.findAll();

        ModelMapper mapper=new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(users, new TypeToken<List<UserDto>>() {}.getType());
    }

    @Override
    public UserDto getUserDetailByEmail(String email) {
        log.info("getUserDetailByEmail 실행");
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if(!userEntity.isPresent()){
            log.info("getUserDetailByEmail 실패");
            throw new UsernameNotFoundException("해당 유저는 존재하지 않습니다.");
        }
        ModelMapper mapper=new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(userEntity.get(), UserDto.class);
        log.info("userDto: "+userDto.getEmail());
        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername "+username);
        Optional<UserEntity> userEntity = userRepository.findByEmail(username);
        log.info(userEntity.get().getEmail()+" "+userEntity.get().getName()+" "+userEntity.get().getEncryptedPwd());
        if(userEntity.isEmpty()){
            throw new UsernameNotFoundException("해당 유저는 존재하지 않습니다.");

        }
        return new User(userEntity.get().getEmail(), userEntity.get().getEncryptedPwd(), true, true, true, true,
                new ArrayList<>());
    }
}
