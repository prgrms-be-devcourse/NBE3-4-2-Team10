package com.ll.TeamProject.domain.calendar;

import com.ll.TeamProject.domain.calendar.controller.CalendarController;
import com.ll.TeamProject.domain.calendar.dto.CalendarCreateDto;
import com.ll.TeamProject.domain.calendar.dto.CalendarUpdateDto;
import com.ll.TeamProject.domain.calendar.entity.Calendar;
import com.ll.TeamProject.domain.calendar.service.CalendarService;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.enums.Role;  // Role enum import
import com.ll.TeamProject.global.userContext.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CalendarControllerTest {

    @Mock
    private CalendarService calendarService;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private CalendarController calendarController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(calendarController).build();
    }

    @Test
    public void testCreateCalendar() throws Exception {
        // 수정된 부분: SiteUser 객체를 생성할 때 Role.USER를 전달
        SiteUser user = new SiteUser(1L, "testUser", "testNickname", Role.USER); // Role.USER를 전달
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

    @Test
    public void testGetAllCalendars() throws Exception {
        SiteUser user = new SiteUser(1L, "testUser", "testNickname", Role.USER); // Role.USER를 전달
        List<Calendar> calendars = List.of(new Calendar(user, "Calendar 1", "Description 1"), new Calendar(user, "Calendar 2", "Description 2"));

        when(userContext.findActor()).thenReturn(java.util.Optional.of(user));
        when(calendarService.getAllCalendars(user.getId())).thenReturn(calendars);

        mockMvc.perform(get("/api/calendars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Calendar 1"))
                .andExpect(jsonPath("$[1].name").value("Calendar 2"));

        verify(calendarService, times(1)).getAllCalendars(user.getId());
    }

    @Test
    public void testGetCalendarById() throws Exception {
        SiteUser user = new SiteUser(1L, "testUser", "testNickname", Role.USER); // Role.USER를 전달
        Calendar calendar = new Calendar(user, "My Calendar", "Description");

        when(userContext.findActor()).thenReturn(java.util.Optional.of(user));
        when(calendarService.getCalendarById(1L)).thenReturn(calendar);

        mockMvc.perform(get("/api/calendars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("My Calendar"))
                .andExpect(jsonPath("$.description").value("Description"));

        verify(calendarService, times(1)).getCalendarById(1L);
    }

    @Test
    public void testUpdateCalendar() throws Exception {
        SiteUser user = new SiteUser(1L, "testUser", "testNickname", Role.USER); // Role.USER를 전달
        CalendarUpdateDto dto = new CalendarUpdateDto();
        dto.setName("Updated Calendar");
        dto.setDescription("Updated Description");
        Calendar calendar = new Calendar(user, "My Calendar", "Description");

        when(userContext.findActor()).thenReturn(java.util.Optional.of(user));
        when(calendarService.getCalendarById(1L)).thenReturn(calendar);
        when(calendarService.updateCalendar(1L, dto)).thenReturn(calendar);

        mockMvc.perform(put("/api/calendars/1")
                        .contentType("application/json")
                        .content("{\"name\": \"Updated Calendar\", \"description\": \"Updated Description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Calendar"))
                .andExpect(jsonPath("$.description").value("Updated Description"));

        verify(calendarService, times(1)).updateCalendar(1L, dto);
    }

    @Test
    public void testDeleteCalendar() throws Exception {
        SiteUser user = new SiteUser(1L, "testUser", "testNickname", Role.USER); // Role.USER를 전달
        Calendar calendar = new Calendar(user, "My Calendar", "Description");

        when(userContext.findActor()).thenReturn(java.util.Optional.of(user));
        when(calendarService.getCalendarById(1L)).thenReturn(calendar);

        mockMvc.perform(delete("/api/calendars/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("캘린더가 삭제되었습니다!"));

        verify(calendarService, times(1)).deleteCalendar(1L);
    }
}