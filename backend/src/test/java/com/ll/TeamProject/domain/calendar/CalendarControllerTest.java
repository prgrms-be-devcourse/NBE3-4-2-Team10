package com.ll.TeamProject.domain.calendar;

import com.ll.TeamProject.domain.calendar.controller.CalendarController;
import com.ll.TeamProject.domain.calendar.dto.CalendarCreateDto;
import com.ll.TeamProject.domain.calendar.dto.CalendarUpdateDto;
import com.ll.TeamProject.domain.calendar.entity.Calendar;
import com.ll.TeamProject.domain.calendar.service.CalendarService;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.enums.Role;
import com.ll.TeamProject.domain.user.repository.UserRepository;
import com.ll.TeamProject.global.userContext.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CalendarControllerTest {

    @Mock
    private CalendarService calendarService;

    @Mock
    private UserContext userContext;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CalendarController calendarController;

    private SiteUser mockUser;
    private CalendarCreateDto calendarCreateDto;
    private CalendarUpdateDto calendarUpdateDto;
    private Calendar mockCalendar;

    @BeforeEach
    public void setUp() {
        // SiteUser 객체 생성 시 Role.USER를 전달
        mockUser = new SiteUser(1L, "username", Role.USER);

        // CalendarCreateDto 객체 생성
        calendarCreateDto = new CalendarCreateDto("10팀의 캘린더", "팀원들과 일정을 관리하는 캘린더.");

        // Calendar 객체 생성
        mockCalendar = new Calendar(mockUser, "10팀의 캘린더", "팀원들과 일정을 관리하는 캘린더.");
    }

    @Test
    public void testCreateCalendar() {
        // Given
        when(userContext.findActor()).thenReturn(Optional.of(mockUser));
        when(calendarService.createCalendar(calendarCreateDto)).thenReturn(mockCalendar);

        // When
        ResponseEntity<Calendar> response = calendarController.createCalendar(calendarCreateDto);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("10팀의 캘린더", response.getBody().getName());
    }

    @Test
    public void testUpdateCalendar() {
        // Given
        Long calendarId = 1L;
        when(userContext.findActor()).thenReturn(Optional.of(mockUser));
        when(calendarService.getCalendarById(calendarId)).thenReturn(mockCalendar);
        when(calendarService.updateCalendar(calendarId, calendarUpdateDto)).thenReturn(mockCalendar);

        // When
        ResponseEntity<?> response = calendarController.updateCalendar(calendarId, calendarUpdateDto);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("10팀의 수정된 캘린더", ((Calendar) response.getBody()).getName());
    }

    @Test
    public void testDeleteCalendar() {
        // Given
        Long calendarId = 1L;
        when(userContext.findActor()).thenReturn(Optional.of(mockUser));
        when(calendarService.getCalendarById(calendarId)).thenReturn(mockCalendar);

        // When
        ResponseEntity<String> response = calendarController.deleteCalendar(calendarId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("캘린더가 삭제되었습니다!", response.getBody());
        verify(calendarService, times(1)).deleteCalendar(calendarId);
    }

    @Test
    public void testGetCalendarById() {
        // Given
        Long calendarId = 1L;
        when(userContext.findActor()).thenReturn(Optional.of(mockUser));
        when(calendarService.getCalendarById(calendarId)).thenReturn(mockCalendar);

        // When
        ResponseEntity<?> response = calendarController.getCalendarById(calendarId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("10팀의 캘린더", ((Calendar) response.getBody()).getName());
    }

    @Test
    public void testGetAllCalendars() {
        // Given
        when(userContext.findActor()).thenReturn(Optional.of(mockUser));
        when(calendarService.getAllCalendars(mockUser.getId())).thenReturn(List.of(mockCalendar));

        // When
        ResponseEntity<List<Calendar>> response = calendarController.getAllCalendars();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }
}