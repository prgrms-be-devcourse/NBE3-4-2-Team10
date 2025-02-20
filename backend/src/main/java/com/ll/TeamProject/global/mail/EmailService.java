package com.ll.TeamProject.global.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements MessageService{

    private final JavaMailSender mailSender;

    @Override
    public void sendMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage(); // 간단한 텍스트 메일
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    @Override
    public void sendVerificationCode(String nickname, String email, String verificationCode) {
        String content = String.format("안녕하세요, %s님.\n\n인증번호: %s\n인증번호는 3분 동안 유효합니다.", nickname, verificationCode);
        sendMessage(email, "계정 인증번호", content);
    }
}
