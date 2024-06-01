package com.backend.martall.domain.user.service;

import com.backend.martall.domain.user.dto.AccountDto;
import com.backend.martall.domain.user.dto.UserDto;
import com.backend.martall.domain.user.entity.Provider;
import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.user.entity.UserRepository;
import com.backend.martall.domain.user.entity.UserType;
import com.backend.martall.global.exception.BadRequestException;
import com.backend.martall.global.exception.ResponseStatus;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, Object> redisTemplate;
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

    public void idInquiryEmailCertification(AccountDto.IdInquiryEmailRequestDto idInquiryEmailRequestDto) {
        // 숫자와 대문자 영어로 구성된 6자리 랜덤 문자열 생성
        String authNumber = RandomStringUtils.randomNumeric(6);

        String toMail = idInquiryEmailRequestDto.getEmail();
        String title = "아이디 찾기 이메일 인증"; // 이메일 제목
        String content =
                "인증 번호 :" + 	//html 형식으로 작성 !
                        "<br>" +
                        authNumber +
                        "<br>" +
                        "인증번호를 입력하고 인증하기를 눌러주세요"; //이메일 내용 삽입

        mailSend(toMail, title, content);

        this.saveVerificationCode(toMail, authNumber);
    }

    public void saveVerificationCode(String email, String authNumber) {
        try {
            // redis에 저장
            // 유지시간 3분
            ValueOperations<String, Object> vop = redisTemplate.opsForValue();
            vop.set(email, authNumber, 3, TimeUnit.MINUTES);
        } catch (RuntimeException e) {
            throw new BadRequestException(ResponseStatus.DATABASE_ERROR);
        }
    }

    //이메일 전송
    public void mailSend(String toMail, String title, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,false,"utf-8");//이메일 메시지와 관련된 설정
            helper.setTo(toMail); //이메일의 수신자 주소 설정
            helper.setFrom("martallofficial@gmail.com");
            helper.setSubject(title); //이메일의 제목을 설정
            helper.setText(content,true); //content 및 html 설정
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new BadRequestException(ResponseStatus.ID_INQUIRY_FAIL_SEND_MAIL);
        }
    }
}
