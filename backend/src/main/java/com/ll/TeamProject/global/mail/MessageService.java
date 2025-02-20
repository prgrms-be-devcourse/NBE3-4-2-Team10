package com.ll.TeamProject.global.mail;

public interface MessageService {
    void sendMessage(String to, String subject, String text);
    void sendVerificationCode(String nickname, String email, String verificationCode);
}
