package com.ll.TeamProject.domain.user.service;

import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.repository.UserRepository;
import com.ll.TeamProject.global.exceptions.ServiceException;
import com.ll.TeamProject.global.mail.GoogleMailService;
import com.ll.TeamProject.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AccountVerificationService {
    private static final String VERIFICATION_CODE_KEY = "verificationCode:";
    private static final String PASSWORD_RESET_KEY = "password-reset:";
    private static final int VERIFICATION_CODE_EXPIRATION = 180;
    private static final int PASSWORD_RESET_EXPIRATION = 300;

    private final UserRepository userRepository;
    private final GoogleMailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    public void processVerification(String username, String email) {
        SiteUser user = validateUsernameAndEmail(username, email);

        String code = generateVerificationCode();
        String redisKey = getKey(VERIFICATION_CODE_KEY, username);

        redisService.setValue(redisKey, code, VERIFICATION_CODE_EXPIRATION);
        sendVerificationEmail(user.getNickname(), user.getEmail(), code);
    }

    private SiteUser validateUsernameAndEmail(String username, String email) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getEmail().equals(email))
                .orElseThrow(() -> new ServiceException("404-1", "아이디 또는 이메일이 일치하지 않습니다."));
    }

    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1000000));
    }

    private void sendVerificationEmail(String nickname, String email, String verificationCode) {
        emailService.sendVerificationCode(nickname, email, verificationCode);
    }

    public void verifyAndUnlockAccount(String username, String verificationCode) {
        String redisKey = getKey(VERIFICATION_CODE_KEY, username);

        redisService.getValue(redisKey)
                .ifPresentOrElse(storedCode -> {

                    if (!verificationCode.equals(storedCode)) {
                        throw new ServiceException("401-3", "인증번호가 일치하지 않습니다.");
                    }

                    redisService.deleteValue(redisKey);
                    redisService.setValue(getKey(PASSWORD_RESET_KEY, username), username, PASSWORD_RESET_EXPIRATION);
                }, () -> {

                    throw new ServiceException("401-3", "인증번호가 만료되었습니다.");
                });
    }

    public void changePassword(String username, String password) {
        String redisKey = getKey(PASSWORD_RESET_KEY, username);
        String storedUsername = redisService.getValue(redisKey)
                .orElseThrow(() -> new ServiceException("401-3", "비밀번호 재설정 요청이 만료되었습니다."));

        if (!username.equals(storedUsername)) {
            redisService.deleteValue(redisKey);
            throw new ServiceException("403-1", "잘못된 요청입니다.");
        }

        SiteUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException("401-1", "존재하지 않는 사용자입니다."));

        user.changePassword(passwordEncoder.encode(password));
        unlockAccount(user);
        redisService.deleteValue(redisKey);
    }

    private void unlockAccount(SiteUser user) {
        user.unlockAccount();
        userRepository.save(user);
    }

    private String getKey(String prefix, String username) {
        return prefix + username;
    }
}
