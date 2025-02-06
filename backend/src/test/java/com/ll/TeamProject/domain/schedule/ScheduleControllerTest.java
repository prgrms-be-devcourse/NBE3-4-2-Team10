package com.ll.TeamProject.domain.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ll.TeamProject.domain.schedule.dto.ScheduleRequestDto;
import com.ll.TeamProject.domain.schedule.entity.Schedule;
import com.ll.TeamProject.domain.schedule.repository.ScheduleRepository;
import com.ll.TeamProject.global.jpa.entity.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql(scripts = "/reset-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // ✅ 테스트 실행 전 DB 초기화
public class ScheduleControllerTest {


    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MockMvc mvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final LocalDateTime tomorrow = LocalDateTime.now().plusHours(24);

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        scheduleRepository.deleteAll();


    }

    LocalDateTime startTime1 = tomorrow.withHour(10).withMinute(0).withSecond(0).withNano(0);
    LocalDateTime endTime1 = startTime1.plusHours(1);

    LocalDateTime startTime2 = tomorrow.withHour(12).withMinute(0).withSecond(0).withNano(0);
    LocalDateTime endTime2 = startTime2.plusHours(1);

    // 포멧 적용
    String formattedStartTime1 = startTime1.format(formatter);
    String formattedEndTime1 = endTime1.format(formatter);
    String formattedStartTime2 = startTime2.format(formatter);
    String formattedEndTime2 = endTime2.format(formatter);

    private Long scheduleId1;
    private Long scheduleId2;

    @BeforeEach
    void setUpTestData() throws Exception {
        Long calendarId = 1L;


        ScheduleRequestDto dto1 = new ScheduleRequestDto("회의 일정",
                "팀 회의",
                startTime1,
                endTime1,
                new Location(37.5665, 126.9780, "서울특별시 중구 세종대로 110")
        );

        ScheduleRequestDto dto2 = new ScheduleRequestDto("운동 일정",
                "헬스장 방문",
                startTime2,
                endTime2,
                new Location(37.5678, 126.9890, "서울특별시 강남구 테헤란로 123")
        );

        String requestBody1 = objectMapper.writeValueAsString(dto1);
        String requestBody2 = objectMapper.writeValueAsString(dto2);

        // 첫 번째 일정 생성 및 scheduleId1 저장
        String responseJson1 = mvc.perform(post("/api/calendars/{calendarId}/schedules", calendarId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody1)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        scheduleId1 = objectMapper.readTree(responseJson1).get("id").asLong();

        // 두 번째 일정 생성 및 scheduleId2 저장
        String responseJson2 = mvc.perform(post("/api/calendars/{calendarId}/schedules", calendarId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody2)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        scheduleId2 = objectMapper.readTree(responseJson2).get("id").asLong();
    }


    @Test
    @DisplayName("일정 생성 성공")
    void t1() throws Exception {
        Long calendarId = 1L;
        scheduleRepository.deleteAll();

        LocalDateTime startTime1 = tomorrow.withHour(10).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endTime1 = startTime1.plusHours(1);

        LocalDateTime startTime2 = tomorrow.withHour(12).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endTime2 = startTime2.plusHours(1);

        // 포맷 적용
        String formattedStartTime1 = startTime1.format(formatter);
        String formattedEndTime1 = endTime1.format(formatter);
        String formattedStartTime2 = startTime2.format(formatter);
        String formattedEndTime2 = endTime2.format(formatter);

        //생성 요청 데이터
        ScheduleRequestDto dto1 = new ScheduleRequestDto("회의 일정",
                "팀 회의",
                startTime1,
                endTime1,
                new Location(37.5665, 126.9780, "서울특별시 중구 세종대로 110")
        );

        ScheduleRequestDto dto2 = new ScheduleRequestDto("운동 일정",
                "헬스장 방문",
                startTime2,
                endTime2,
                new Location(37.5678, 126.9890, "서울특별시 강남구 테헤란로 123")
        );

        String requestBody1 = objectMapper.writeValueAsString(dto1);
        String requestBody2 = objectMapper.writeValueAsString(dto2);

        // Perform POST 요청
        ResultActions resultActions1 = mvc.perform(
                        post("/api/calendars/{calendarId}/schedules", calendarId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody1)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        ResultActions resultActions2 = mvc.perform(
                        post("/api/calendars/{calendarId}/schedules", calendarId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody2)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        // 기대 응답 검증
        resultActions1
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("회의 일정"))
                .andExpect(jsonPath("$.description").value("팀 회의"))
                .andExpect(jsonPath("$.startTime").value(formattedStartTime1))
                .andExpect(jsonPath("$.endTime").value(formattedEndTime1))
                .andExpect(jsonPath("$.location.latitude").value(37.5665))
                .andExpect(jsonPath("$.location.longitude").value(126.9780))
                .andExpect(jsonPath("$.location.address").value("서울특별시 중구 세종대로 110"));

        resultActions2
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("운동 일정"))
                .andExpect(jsonPath("$.description").value("헬스장 방문"))
                .andExpect(jsonPath("$.startTime").value(formattedStartTime2))
                .andExpect(jsonPath("$.endTime").value(formattedEndTime2))
                .andExpect(jsonPath("$.location.latitude").value(37.5678))
                .andExpect(jsonPath("$.location.longitude").value(126.9890))
                .andExpect(jsonPath("$.location.address").value("서울특별시 강남구 테헤란로 123"));
    }


    @Test
    @DisplayName("일정 생성 실패 - 중복 시간")
    void t2() throws Exception {
        Long calendarId = 1L;

        LocalDateTime startTime = startTime1; // 기존 일정과 동일한 시작 시간
        LocalDateTime endTime = endTime1; // 기존 일정과 동일한 종료 시간

        // 중복 시간 요청 데이터
        ScheduleRequestDto dto = new ScheduleRequestDto("중복 시간 테스트",
                "겹치는 일정",
                startTime,
                endTime,
                new Location(37.5665, 126.9780, "서울특별시 중구 세종대로 110")
        );

        String requestBody = objectMapper.writeValueAsString(dto);

        // Perform POST 요청
        ResultActions resultActions = mvc.perform(
                        post("/api/calendars/{calendarId}/schedules", calendarId)
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        // 기대 응답 검증
        resultActions
                .andExpect(status().isBadRequest()) // 400 응답 확인
                .andExpect(jsonPath("$.msg").value("해당 시간에 이미 일정이 존재합니다.")); // 예외 메시지 확인
    }



    @Test
    @DisplayName("일정 목록 조회 - 성공")
    void t3() throws Exception {

        Long calendarId = 1L;

        // Perform GET 요청
        ResultActions resultActions = mvc.perform(
                        get("/api/calendars/{calendarId}/schedules", calendarId)
                                .param("startDate", tomorrow.toLocalDate().toString())
                                .param("endDate", tomorrow.toLocalDate().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        // 기대 응답 검증
        resultActions
                .andExpect(status().isOk()) // 상태 코드 200 확인
                .andExpect(jsonPath("$.length()").value(2)) // 응답 배열 크기 검증
                .andExpect(jsonPath("$[0].title").value("회의 일정"))
                .andExpect(jsonPath("$[0].description").value("팀 회의"))
                .andExpect(jsonPath("$[0].startTime").value(formattedStartTime1))
                .andExpect(jsonPath("$[0].endTime").value(formattedEndTime1))
                .andExpect(jsonPath("$[0].location.latitude").value(37.5665))
                .andExpect(jsonPath("$[0].location.longitude").value(126.9780))
                .andExpect(jsonPath("$[0].location.address").value("서울특별시 중구 세종대로 110"))
                .andExpect(jsonPath("$[1].title").value("운동 일정"))
                .andExpect(jsonPath("$[1].description").value("헬스장 방문"))
                .andExpect(jsonPath("$[1].startTime").value(formattedStartTime2))
                .andExpect(jsonPath("$[1].endTime").value(formattedEndTime2))
                .andExpect(jsonPath("$[1].location.latitude").value(37.5678))
                .andExpect(jsonPath("$[1].location.longitude").value(126.9890))
                .andExpect(jsonPath("$[1].location.address").value("서울특별시 강남구 테헤란로 123"));
    }



    @Test
    @DisplayName("일정 목록 조회 - 일정 없음")
    void t4() throws Exception {
        Long calendarId = 1L;

        // Perform GET 요청
        ResultActions resultActions = mvc.perform(
                        get("/api/calendars/{calendarId}/schedules", calendarId)
                                .param("startDate", "2025-03-01")
                                .param("endDate", "2025-03-02")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        // 결과 검증
        resultActions
                .andExpect(status().isOk()) // 상태 코드 200 확인
                .andExpect(jsonPath("$.length()").value(0)); // 조회 데이터 길이
    }

    @Test
    @DisplayName("특정 일정 조회 성공")
    void t5() throws Exception {
        Long calendarId = 1L;

        // Perform GET 요청
        ResultActions resultActions = mvc.perform(
                        get("/api/calendars/{calendarId}/schedules/{scheduleId}", calendarId, scheduleId1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        // 기대 응답 검증
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(scheduleId1))
                .andExpect(jsonPath("$.title").value("회의 일정"))
                .andExpect(jsonPath("$.description").value("팀 회의"))
                .andExpect(jsonPath("$.startTime").value(formattedStartTime1))
                .andExpect(jsonPath("$.endTime").value(formattedEndTime1))
                .andExpect(jsonPath("$.location.latitude").value(37.5665))
                .andExpect(jsonPath("$.location.longitude").value(126.9780))
                .andExpect(jsonPath("$.location.address").value("서울특별시 중구 세종대로 110"));
    }

    @Test
    @DisplayName("특정 일정 조회 실패 - 일정이 존재하지 않음")
    void t6() throws Exception {
        Long calendarId = 1L;

        // 현재 DB에 존재하는 가장 높은 scheduleId 가져오기
        Long maxScheduleId = scheduleRepository.findTopByOrderByIdDesc()
                .map(Schedule::getId)
                .orElse(0L);

        Long nonExistentScheduleId = maxScheduleId + 1; // 존재하지 않는 ID 설정

        // Perform GET 요청
        ResultActions resultActions = mvc.perform(
                get("/api/calendars/{calendarId}/schedules/{scheduleId}", calendarId, nonExistentScheduleId) // 동적으로 존재하지 않는 ID 사용
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
        ).andDo(print());

        // 기대 응답 검증
        resultActions
                .andExpect(status().isNotFound()) // 404 응답 확인
                .andExpect(jsonPath("$.msg").value("해당 일정을 찾을 수 없습니다."));
    }



    @Test
    @DisplayName("일정 수정 성공")
    void t7() throws Exception {
        Long calendarId = 1L;
        Long scheduleId = scheduleId1;

        LocalDateTime startTime = tomorrow.withHour(17).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endTime = startTime.plusHours(3);

        String formattedStartTime = startTime.format(formatter);
        String formattedEndTime = endTime.format(formatter);

        // 수정 요청 데이터
        ScheduleRequestDto updateDto = new ScheduleRequestDto(
                "수정된 일정 제목",
                "수정된 일정 설명",
                startTime,
                endTime,
                new Location(37.5678, 126.9890, "서울특별시 중구 세종대로 110")
        );

        String requestBody = objectMapper.writeValueAsString(updateDto);

        // Perform PUT 요청
        ResultActions resultActions = mvc.perform(
                put("/api/calendars/{calendarId}/schedules/{scheduleId}", calendarId, scheduleId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
        ).andDo(print());

        // 기대 응답 검증
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정된 일정 제목"))
                .andExpect(jsonPath("$.description").value("수정된 일정 설명"))
                .andExpect(jsonPath("$.startTime").value(formattedStartTime))
                .andExpect(jsonPath("$.endTime").value(formattedEndTime))
                .andExpect(jsonPath("$.location.latitude").value(37.5678))
                .andExpect(jsonPath("$.location.longitude").value(126.9890))
                .andExpect(jsonPath("$.location.address").value("서울특별시 중구 세종대로 110"));
    }


    @Test
    @DisplayName("일정 수정 실패 - 일정이 존재하지 않음")
    void t8() throws Exception {
        Long calendarId = 1L;

        // 현재 DB에 존재하는 가장 높은 scheduleId 가져오기
        Long maxScheduleId = scheduleRepository.findTopByOrderByIdDesc()
                .map(Schedule::getId)
                .orElse(0L);

        Long nonExistentScheduleId = maxScheduleId + 1; // 존재하지 않는 ID 설정

        LocalDateTime startTime = tomorrow.withHour(17).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endTime = startTime.plusHours(3);

        // 수정 요청 데이터
        ScheduleRequestDto updateDto = new ScheduleRequestDto(
                "수정된 일정 제목",
                "수정된 일정 설명",
                startTime,
                endTime,
                new Location(37.5678, 126.9890, "서울특별시 중구 세종대로 110")
        );

        String requestBody = objectMapper.writeValueAsString(updateDto);

        // Perform PUT 요청
        ResultActions resultActions = mvc.perform(
                put("/api/calendars/{calendarId}/schedules/{scheduleId}", calendarId, nonExistentScheduleId) // 존재하지 않는 ID 사용
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
        ).andDo(print());

        // 기대 응답 검증
        resultActions
                .andExpect(status().isNotFound()) // 404 응답 확인
                .andExpect(jsonPath("$.msg").value("해당 일정을 찾을 수 없습니다."));
    }


    @Test
    @DisplayName("일정 수정 실패 - 시간 충돌")
    void t9() throws Exception {
        Long calendarId = 1L;
        Long scheduleId = scheduleId1;

        // 기존 일정과 동일한 시간으로 업데이트하여 시간 충돌 발생 유도
        LocalDateTime startTime = startTime2; // scheduleId=2 일정과 동일한 시작 시간
        LocalDateTime endTime = endTime2; // scheduleId=2 일정과 동일한 종료 시간

        // 수정 요청 데이터
        ScheduleRequestDto updateDto = new ScheduleRequestDto(
                "겹치는 시간 수정 요청",
                "겹치는 일정 테스트",
                startTime,
                endTime,
                new Location(37.5678, 126.9890, "서울특별시 중구 세종대로 110")
        );

        String requestBody = objectMapper.writeValueAsString(updateDto);

        // Perform PUT 요청 (시간 충돌 발생 예상)
        ResultActions resultActions = mvc.perform(
                put("/api/calendars/{calendarId}/schedules/{scheduleId}", calendarId, scheduleId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
        ).andDo(print());

        // 기대 응답 검증
        resultActions
                .andExpect(status().isBadRequest()) // 400 응답 확인
                .andExpect(jsonPath("$.msg").value("해당 시간에 이미 일정이 존재합니다.")); // 예외 메시지 확인
    }


    @Test
    @DisplayName("일정 삭제 성공")
    void t10() throws Exception {
        Long calendarId = 1L;
        Long scheduleId = scheduleId1;

        // Perform DELETE 요청
        ResultActions resultActions = mvc.perform(
                        delete("/api/calendars/{calendarId}/schedules/{scheduleId}", calendarId, scheduleId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        // 기대 응답
        resultActions
                .andExpect(status().isNoContent()); // 상태 코드 204 (No Content)
    }

    @Test
    @DisplayName("일정 삭제 실패 - 일정이 존재하지 않음")
    void t11() throws Exception {
        Long calendarId = 1L;

        // 현재 DB에 가장 높은 scheduleId 가져오기
        Long maxScheduleId = scheduleRepository.findTopByOrderByIdDesc()
                .map(Schedule::getId)
                .orElse(0L);

        Long nonExistentScheduleId = maxScheduleId + 1; // 존재하지 않는 ID 설정

        // Perform DELETE 요청
        ResultActions resultActions = mvc.perform(
                delete("/api/calendars/{calendarId}/schedules/{scheduleId}", calendarId, nonExistentScheduleId) // 존재하지 않는 ID 사용
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
        ).andDo(print());

        // 기대 응답 검증
        resultActions
                .andExpect(status().isNotFound()) // 404 응답 확인
                .andExpect(jsonPath("$.msg").value("해당 일정을 찾을 수 없습니다.")); // 예외 메시지 확인
    }

}