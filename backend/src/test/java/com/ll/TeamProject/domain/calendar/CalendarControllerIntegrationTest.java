package com.ll.TeamProject.domain.calendar;

import com.ll.TeamProject.domain.calendar.dto.CalendarCreateDto;
import com.ll.TeamProject.domain.calendar.dto.CalendarUpdateDto;
import com.ll.TeamProject.domain.calendar.entity.Calendar;
import com.ll.TeamProject.domain.calendar.repository.CalendarRepository;
import com.ll.TeamProject.domain.user.entity.SiteUser;
import com.ll.TeamProject.domain.user.enums.Role;
import com.ll.TeamProject.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CalendarControllerIntegrationTest {

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private UserRepository userRepository;

    private SiteUser testUser;
    private CalendarCreateDto calendarCreateDto;
    private CalendarUpdateDto calendarUpdateDto;

    @BeforeEach
    public void setUp() {
        // testUser 생성 (ID를 명시적으로 설정)
        testUser = new SiteUser(1L, "username", "password", Role.USER);  // 1L은 long 타입의 ID를 의미
        userRepository.save(testUser);

        // CalendarCreateDto 생성
        calendarCreateDto = new CalendarCreateDto();
        calendarCreateDto.setName("10팀의 캘린더");
        calendarCreateDto.setDescription("팀원들과 일정을 관리하는 캘린더.");
    }

    @Test
    public void testCreateCalendar() {
        // Given
        Calendar createdCalendar = new Calendar(testUser, calendarCreateDto.getName(), calendarCreateDto.getDescription());
        calendarRepository.save(createdCalendar);

        // When
        List<Calendar> calendars = calendarRepository.findByUserId(testUser.getId());

        // Then
        assertNotNull(calendars);
        assertFalse(calendars.isEmpty());
        assertEquals(1, calendars.size());
        assertEquals("10팀의 캘린더", calendars.get(0).getName());
    }

    @Test
    public void testGetCalendarById() {
        // Given
        Calendar createdCalendar = new Calendar(testUser, calendarCreateDto.getName(), calendarCreateDto.getDescription());
        Calendar savedCalendar = calendarRepository.save(createdCalendar);

        // When
        Calendar foundCalendar = calendarRepository.findById(savedCalendar.getId()).orElse(null);

        // Then
        assertNotNull(foundCalendar);
        assertEquals("10팀의 캘린더", foundCalendar.getName());
    }

    @Test
    public void testUpdateCalendar() {
        // Given
        Calendar createdCalendar = new Calendar(testUser, calendarCreateDto.getName(), calendarCreateDto.getDescription());
        Calendar savedCalendar = calendarRepository.save(createdCalendar);

        // When
        CalendarUpdateDto updateDto = new CalendarUpdateDto();
        updateDto.setName("10팀의 수정된 캘린더");
        updateDto.setDescription("수정된 설명");  // description도 설정

        // Calendar 객체를 업데이트하는 부분을 추가
        savedCalendar.update(updateDto);  // update 메서드를 호출하여 Calendar 엔티티를 수정

        // Save updated calendar
        calendarRepository.save(savedCalendar);

        // Then
        Calendar foundCalendar = calendarRepository.findById(savedCalendar.getId()).orElse(null);
        assertNotNull(foundCalendar);
        assertEquals("10팀의 수정된 캘린더", foundCalendar.getName());  // 수정된 이름을 검증
        assertEquals("수정된 설명", foundCalendar.getDescription());  // 수정된 설명을 검증
    }

    @Test
    public void testDeleteCalendar() {
        // Given
        Calendar createdCalendar = new Calendar(testUser, calendarCreateDto.getName(), calendarCreateDto.getDescription());
        Calendar savedCalendar = calendarRepository.save(createdCalendar);

        // When
        calendarRepository.delete(savedCalendar);

        // Then
        Calendar foundCalendar = calendarRepository.findById(savedCalendar.getId()).orElse(null);
        assertNull(foundCalendar);
    }

    @Test
    public void testGetAllCalendars() {
        // Given
        Calendar createdCalendar1 = new Calendar(testUser, "캘린더1", "설명1");
        Calendar createdCalendar2 = new Calendar(testUser, "캘린더2", "설명2");
        calendarRepository.save(createdCalendar1);
        calendarRepository.save(createdCalendar2);

        // When
        List<Calendar> calendars = calendarRepository.findByUserId(testUser.getId());

        // Then
        assertNotNull(calendars);
        assertFalse(calendars.isEmpty());
        assertEquals(2, calendars.size());
    }
}