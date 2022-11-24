package com.zerobase.cms.user.application;

import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.client.mailgun.SendMailForm;
import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.service.SignUpCustomerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SignUpApplication {
    private final MailgunClient mailgunClient;
    private final SignUpCustomerService customerService;

    public void customerVerify(String email, String code) {
        customerService.verifyEmail(email, code);
    }

    public String customerSignUp(SignUpForm form) {
        if (customerService.isEmailExist(form.getEmail())) {
            // exception
            throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
        } else {
            Customer c = customerService.signUp(form);  // 회원가입
            LocalDateTime now = LocalDateTime.now();
            String code = getRandomCode();

            SendMailForm mailForm = SendMailForm.builder()
                    .from("any@test.com")
                    .to(form.getEmail())
                    .subject("Verification Email!")
                    .text(getVerificationEmailBody(form.getEmail(), form.getName(), code))
                    .build();

            mailgunClient.sendEmail(mailForm);  // 인증메일전송
            customerService.changeCustomerValidateEmail(c.getId(), code);   // 해당유저의 이메일 인증 정보 수정
            return "회원 가입에 성공하였습니다.";
        }
    }

    private String getRandomCode() {
        // 10자리, 단어포함 true, 숫자포함 true
        return RandomStringUtils.random(10, true, true);
    }

    private String getVerificationEmailBody(String email, String name, String code) {
        StringBuilder builder = new StringBuilder();

        return builder.append("Hello ").append(name).append("! Please Click link for verification.\n\n")
                .append("https://localhost:8081/signup/verify/customer?email=")
                .append(email)
                .append("&code=")
                .append(code).toString();
    }
}
