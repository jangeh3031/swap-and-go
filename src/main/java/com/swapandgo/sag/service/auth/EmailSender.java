package com.swapandgo.sag.service.auth;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSender {
    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from-email}")
    private String fromEmail;

    @Async
    public void sendVerificationEmail(String to, String code){
        try {
            Email from = new Email(fromEmail);
            Email toEmail = new Email(to);
            String subject = "[SwapAndGo] 이메일 인증 코드";
            String body = "안녕하세요.\n\n" +
                    "회원가입을 위한 인증코드는 다음과 같습니다 : \n\n" + code + "\n\n" +
                    "이 코드는 5분간 유효합니다. \n";

            Content content = new Content("text/plain", body);
            Mail mail = new Mail(from, subject, toEmail, content);

            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                log.info("인증 이메일 발송 완료 {} status={}", to, response.getStatusCode());
            } else {
                log.error("인증 이메일 발송 실패 {} status={} body={}", to, response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            log.error("인증 이메일 발송 실패 {}: {}", to, e.getMessage());
        }
    }
}
