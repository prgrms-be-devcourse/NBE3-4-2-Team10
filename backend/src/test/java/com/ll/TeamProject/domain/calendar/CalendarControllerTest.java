package com.ll.TeamProject.domain.calendar;

import com.ll.TeamProject.domain.calendar.controller.CalendarController;
import com.ll.TeamProject.domain.calendar.dto.CalendarCreateDto;
import com.ll.TeamProject.domain.calendar.entity.Calendar;
import com.ll.TeamProject.domain.calendar.service.CalendarService;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.enums.Role;
import com.ll.TeamProject.global.userContext.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class CalendarControllerTest {

    @Mock
    private CalendarService calendarService;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private CalendarController calendarController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        // Spring Security 필터를 자동으로 설정할 수 있게 변경
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER") // 가짜 로그인 유저 설정
    public void testCreateCalendar() throws Exception {
        SiteUser user = new SiteUser(1L, "testUser", "testNickname", Role.USER);
        CalendarCreateDto dto = new CalendarCreateDto();
        dto.setUserId(1L);
        dto.setName("My Calendar");
        dto.setDescription("Description");

        Calendar calendar = new Calendar(user, dto.getName(), dto.getDescription());

        when(userContext.findActor()).thenReturn(java.util.Optional.of(user));
        when(calendarService.createCalendar(any(CalendarCreateDto.class))).thenReturn(calendar);

        mockMvc.perform(post("/api/calendars")
                        .contentType("application/json")
                        .content("{\"userId\": 1, \"name\": \"My Calendar\", \"description\": \"Description\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("My Calendar"))
                .andExpect(jsonPath("$.description").value("Description"));

        verify(calendarService, times(1)).createCalendar(any(CalendarCreateDto.class));
    }
}