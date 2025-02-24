package com.ll.TeamProject.domain.user.service;

import com.ll.TeamProject.domain.user.dto.UserDto;
import com.ll.TeamProject.domain.user.dto.LoginDto;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.repository.UserRepository;
import com.ll.TeamProject.global.exceptions.CustomException;
import com.ll.TeamProject.domain.user.exceptions.UserErrorCode;
import com.ll.TeamProject.global.exceptions.ServiceException;
import com.ll.TeamProject.global.userContext.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final UserContext userContext;
    private final PasswordEncoder passwordEncoder;

    public LoginDto login(String username, String password) {

        SiteUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(UserErrorCode.INVALID_CREDENTIALS));

        if (user.isLocked()) throw new ServiceException("403-2", "계정이 잠겨있습니다.");

        if (!passwordEncoder.matches(password, user.getPassword())) {
            authenticationService.handleLoginFailure(user);
            throw new ServiceException("401-2", "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return createLoginResponse(user);
    }

    private LoginDto createLoginResponse(SiteUser user) {
        authenticationService.modifyLastLogin(user);
        String accessToken = userContext.makeAuthCookies(user);
        return new LoginDto(new UserDto(user), user.getApiKey(), accessToken);
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
