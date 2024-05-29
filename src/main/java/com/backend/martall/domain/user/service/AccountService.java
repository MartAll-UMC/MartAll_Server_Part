package com.backend.martall.domain.user.service;

import com.backend.martall.domain.user.dto.UserDto;
import com.backend.martall.domain.user.entity.Provider;
import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.user.entity.UserRepository;
import com.backend.martall.domain.user.entity.UserType;
import com.backend.martall.global.exception.BadRequestException;
import com.backend.martall.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;

    public void joinInApp(UserDto.UserJoinDto userJoinDto) {

        // 같은 아이디의 회원이 있는지 검사
        if(userRepository.existsById(userJoinDto.getId())) {
            throw new BadRequestException(ResponseStatus.ALREADY_EXISTS_USER_ID);
        }

        // 비밀번호 인코더
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 유저 엔티티 생성
        User newUser = User.builder()
                .username(userJoinDto.getName())
                .id(userJoinDto.getId())
                .password(encoder.encode(userJoinDto.getPassword()))
                .email((userJoinDto.getEmail()))
                .provider(Provider.IN_APP.getName())
                .userType(UserType.USER.getTypeCode())
                .build();

        // 유저 저장
        userRepository.save(newUser);

    }

    public UserDto.IdDupCheckResponseDto joinIdDupCheck(UserDto.IdDupCheckRequestDto idDupCheckRequestDto) {

        return UserDto.IdDupCheckResponseDto.builder()
                .idDupCheck(!userRepository.existsById(idDupCheckRequestDto.getId()))
                .build();
    }
}
