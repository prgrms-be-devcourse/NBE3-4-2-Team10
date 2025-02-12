package com.ll.TeamProject.domain.user.service;

import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.enums.Role;
import com.ll.TeamProject.domain.user.repository.UserRepository;
import com.ll.TeamProject.global.exceptions.ServiceException;
import com.ll.TeamProject.global.userContext.UserContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Mockito 확장 사용
class UserServiceTest {

    @InjectMocks
    private UserService userService; // 테스트할 클래스

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Page<SiteUser> mockPage;

    @Mock
    private UserContext userContext;

    @Test
    @DisplayName("로그인시 잘못된 아이디")
    void t1() {
        // given
        String username = "nonexistentUser";
        String password = "password";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // when & then
        ServiceException exception = assertThrows(ServiceException.class,
                () -> userService.login(username, password));

        assertEquals("401-1", exception.getResultCode());
        assertEquals("존재하지 않는 사용자입니다.", exception.getMsg());
    }

    @Test
    @DisplayName("로그인시 잘못된 비밀번호")
    void t2() {
        // given
        String username = "username";
        String password = "wrongPassword";
        SiteUser user = SiteUser.builder().username(username).password(passwordEncoder.encode("123")).build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(applicationContext.getBean(PasswordEncoder.class)).thenReturn(passwordEncoder);

        // when & then
        ServiceException exception = assertThrows(ServiceException.class,
                () -> userService.login(username, password));

        assertEquals("401-2", exception.getResultCode());
        assertEquals("비밀번호가 일치하지 않습니다.", exception.getMsg());

        verify(authenticationService).handleLoginFailure(user); // 로그인 실패 처리 실행 확인
    }

    @Test
    @DisplayName("회원 명단 조회 - 빈 키워드")
    void t3() {
        String searchKeywordType = "username";
        String searchKeyword = "";
        int page = 1;
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        UserService spyService = spy(userService);
        doReturn(mockPage).when(spyService).findUsersNoKeyword(pageRequest, Role.USER);

        // when
        spyService.findUsers(searchKeywordType, searchKeyword, page, pageSize, Role.USER);

        // then
        verify(spyService).findUsersNoKeyword(pageRequest, Role.USER);
    }

    @Test
    @DisplayName("회원 명단 조회 - 이메일로")
    void t4() {
        // given
        String searchKeywordType = "email";
        String searchKeyword = "test@example.com";
        int page = 1;
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        when(userRepository.findByRoleAndEmailLikeAndIsDeletedFalse(Role.USER, "%" + searchKeyword + "%", pageRequest))
                .thenReturn(mockPage);

        // when
        userService.findUsers(searchKeywordType, searchKeyword, page, pageSize, Role.USER);

        // then
        verify(userRepository)
                .findByRoleAndEmailLikeAndIsDeletedFalse(Role.USER, "%" + searchKeyword + "%", pageRequest);
    }

    @Test
    @DisplayName("회원 명단 조회 - username 으로")
    void t5() {
        // given
        String searchKeywordType = "username";
        String searchKeyword = "testUser";
        int page = 1;
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

        when(userRepository.findByRoleAndUsernameLikeAndIsDeletedFalse(Role.USER, "%" + searchKeyword + "%", pageRequest))
                .thenReturn(mockPage);

        // when
        userService.findUsers(searchKeywordType, searchKeyword, page, pageSize, Role.USER);

        // then
        verify(userRepository)
                .findByRoleAndUsernameLikeAndIsDeletedFalse(Role.USER, "%" + searchKeyword + "%", pageRequest);
    }

    @Test
    @DisplayName("회원 명단 조회 - 잘못된 페이지")
    void t6() {
        // given
        String searchKeywordType = "username";
        String searchKeyword = "";
        int page = 0;
        int pageSize = 10;

        // when & then
        ServiceException exception = assertThrows(ServiceException.class,
                () -> userService.findUsers(searchKeywordType, searchKeyword, page, pageSize, Role.USER));

        assertEquals("400-1", exception.getResultCode());
        assertEquals("페이지 번호는 1 이상이어야 합니다.", exception.getMsg());
    }

    @Test
    @DisplayName("회원 탈퇴 - 권한 없음")
    @WithMockUser(username = "actor", roles = "USER")
    void t7() {
        SiteUser userToDelete = SiteUser.builder().username("userToDelete").build();
        SiteUser actor = SiteUser.builder().username("actor").build();

        when(userContext.getActor())
                .thenReturn(actor);

        ServiceException exception = assertThrows(ServiceException.class,
                () -> userService.validatePermission(userToDelete));

        assertEquals("403-1", exception.getResultCode());
        assertEquals("접근 권한이 없습니다.", exception.getMsg());
    }
}
