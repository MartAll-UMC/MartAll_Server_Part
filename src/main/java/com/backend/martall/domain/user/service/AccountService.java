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

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, Object> redisTemplate;

    // 인앱 회원가입
    public void joinInApp(UserDto.UserJoinDto userJoinDto) {

        // 같은 아이디의 회원이 있는지 검사
        if (userRepository.existsById(userJoinDto.getId())) {
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

    // 아이디 중복 검사
    public UserDto.IdDupCheckResponseDto joinIdDupCheck(UserDto.IdDupCheckRequestDto idDupCheckRequestDto) {

        return UserDto.IdDupCheckResponseDto.builder()
                .idDupCheck(!userRepository.existsById(idDupCheckRequestDto.getId()))
                .build();
    }

    //
    public void requestEmailCertification(AccountDto.IdInquiryEmailRequestDto idInquiryEmailRequestDto) {
        // 인증번호 : 숫자와 대문자 영어로 구성된 6자리 랜덤 문자열 생성
        String authNumber = RandomStringUtils.randomNumeric(6);

        String toMail = idInquiryEmailRequestDto.getEmail();
        String title = "아이디 찾기 이메일 인증"; // 이메일 제목
        String content = // 이메일 내용 html 형식으로 작성
                "인증 번호 :" +
                        "<br>" +
                        authNumber +
                        "<br>" +
                        "인증번호를 입력하고 인증하기를 눌러주세요";

        mailSend(toMail, title, content);

        this.saveCertificationCode(toMail, authNumber);
    }

    // 아이디찾기 이메일 인증 코드 확인
    public AccountDto.IdInquiryCertificationCodeResponseDto responseEmailCertification(
            AccountDto.IdInquiryCertificationCodeRequestDto idInquiryCertificationCodeRequestDto) {

        return AccountDto.IdInquiryCertificationCodeResponseDto.builder()
                .emailCheck(checkCertificationCode(idInquiryCertificationCodeRequestDto.getEmail(),
                        idInquiryCertificationCodeRequestDto.getVerificationCode()))
                .build();
    }

    // redis에 인증 코드 저장
    // key : email
    // 유지시간 : 3분
    public void saveCertificationCode(String email, String authNumber) {
        try {
            ValueOperations<String, Object> vop = redisTemplate.opsForValue();
            vop.set(email, authNumber, 3, TimeUnit.MINUTES);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new BadRequestException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public boolean checkCertificationCode(String email, String authNumber) {
        try {
            ValueOperations<String, Object> vop = redisTemplate.opsForValue();
            // authNumber가 redis에 있는 인증번호와 같으면 true
            return vop.get(email).equals(authNumber);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new BadRequestException(ResponseStatus.ID_INQUIRY_FAIL_NOT_EXIST_MAIL);
        }
    }

    //이메일 전송
    public void mailSend(String toMail, String title, String content) {
        //JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");//이메일 메시지와 관련된 설정
            helper.setTo(toMail); //이메일의 수신자 주소 설정
            helper.setFrom("martallofficial@gmail.com");
            helper.setSubject(title); //이메일의 제목을 설정
            helper.setText(content, true); //content 및 html 설정
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new BadRequestException(ResponseStatus.ID_INQUIRY_FAIL_SEND_MAIL);
        }
    }

    public List<AccountDto.IdInquiryResponseDto> idInquiry(AccountDto.IdInquiryRequestDto idInquiryRequestDto) {

        List<User> userList = userRepository.findAllByUsernameAndEmail(idInquiryRequestDto.getName(), idInquiryRequestDto.getEmail());

        if(userList.isEmpty()) {
            throw new BadRequestException(ResponseStatus.ID_INQUIRY_WRONG_NAME_AND_EMAIL);
        }

        List<AccountDto.IdInquiryResponseDto> idInquiryResponseDtoList = userList.stream()
                .map(user -> {
                    return AccountDto.IdInquiryResponseDto.builder()
                            .id(maskId(user.getId()))
                            .registerDate(user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                            .build();
                })
                .collect(Collectors.toList());

        return idInquiryResponseDtoList;
    }

    public String maskId(String id) {
        StringBuilder maskedId = new StringBuilder();

        maskedId.append((id.substring(0, id.length()-4)));

        for (int i = id.length()-4; i < id.length(); i++) {
            maskedId.append("*");
        }

        return maskedId.toString();
    }
}
