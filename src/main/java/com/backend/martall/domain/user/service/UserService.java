package com.backend.martall.domain.user.service;

import com.backend.martall.domain.user.dto.UserDto;
import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.user.entity.UserRepository;
import com.backend.martall.domain.user.jwt.JwtTokenProvider;
import com.backend.martall.global.exception.GlobalException;
import com.backend.martall.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    public String join(UserDto.UserRequestDto userRequestDto) {
        String accessToken = null;
        
        if(userRequestDto.getProvider().isBlank() || userRequestDto.getProvider().isEmpty()) {
            throw new GlobalException(ResponseStatus.LOGIN_FAIL_EMPTY_PROVIDER);
        }

        if(userRequestDto.getProvider().equals("kakao")){
            if(userRequestDto.getProviderId() == null) {
                throw new GlobalException(ResponseStatus.LOGIN_FAIL_EMPTY_PROVIDER_ID);
            }

            Optional<User> optionalUser = userRepository.findByProviderId(userRequestDto.getProviderId());
            if(optionalUser.isEmpty()) { //join

                User user = User.builder()
                        .id(userRequestDto.getId())
                        .password(userRequestDto.getPassword()) //== null ? null : passwordEncoder.encode(userRequestDto.getPassword()))
                        .username(userRequestDto.getUsername())
                        .imgUrl(userRequestDto.getImgUrl())
                        .email(userRequestDto.getEmail())
                        .phoneNumber(userRequestDto.getPhoneNumber())
                        .provider(userRequestDto.getProvider())
                        .providerId(userRequestDto.getProviderId())
                        .userType(userRequestDto.getUserType())
                        .userState(0)
                        .money(0)
                        .fcmToken(null)
                        .build();

                try {
                    userRepository.save(user);
                } catch (Exception e) {
                    throw new GlobalException(ResponseStatus.DATABASE_ERROR);
                }

            }

            User user = userRepository.findByProviderId(userRequestDto.getProviderId()).get();
            accessToken = jwtTokenProvider.createAccessToken(user.getUserIdx());
        }
        
        return accessToken;
        
        //자체 로그인 시 예외처리 예정
        //이메일 검사
        //필수인데 존재하지 않는 정보 검사
        //길이 검사
    }
}
