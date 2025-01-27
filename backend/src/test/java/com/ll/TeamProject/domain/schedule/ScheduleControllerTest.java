package com.ll.TeamProject.domain.schedule;

import com.ll.TeamProject.domain.schedule.controller.ScheduleController;
import com.ll.TeamProject.domain.schedule.dto.ScheduleResponseDto;
import com.ll.TeamProject.domain.schedule.repository.ScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.Result;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WebMvcTest(ScheduleController.class)
public class ScheduleControllerTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("일정 생성 성공")
    void t1() throws Exception {
        scheduleRepository.deleteAll();

        // 요청 데이터 예시
        String requestBody = """
        [
            {
                "title": "회의 일정",
                "description": "팀 회의",
                "startTime": "2025-02-01T10:00:00",
                "endTime": "2025-02-01T11:00:00",
                "location": {
                    "latitude": 37.5665,
                    "longitude": 126.9780,
                    "address": "서울특별시 중구 세종대로 110"
                }
            },
            {
                "title": "운동 일정",
                "description": "헬스장 방문",
                "startTime": "2025-02-02T15:00:00",
                "endTime": "2025-02-02T16:00:00",
                "location": {
                    "latitude": 37.5678,
                    "longitude": 126.9890,
                    "address": "서울특별시 강남구 테헤란로 123"
                }
            }
        ]
        """;


        // Perform POST 요청
        ResultActions resultActions = mvc.perform(
                        post("/schedules")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        // 기대 응답
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("회의 일정"))
                .andExpect(jsonPath("$.description").value("팀 회의"))
                .andExpect(jsonPath("$.startTime").value("2025-02-01T10:00:00"))
                .andExpect(jsonPath("$.endTime").value("2025-02-01T11:00:00"))
                .andExpect(jsonPath("$.location.latitude").value(37.5665))
                .andExpect(jsonPath("$.location.longitude").value(126.9780))
                .andExpect(jsonPath("$.location.address").value("서울특별시 중구 세종대로 110"));
    }

    @Test
    @DisplayName("일정 생성 실패 - 중복 시간")
    void t2() throws Exception {
        // 중복 시간 요청 데이터
        String requestBody = """
            {
                "title": "중복 시간 테스트",
                "description": "겹치는 일정",
                "startTime": "2025-02-01T10:30:00",
                "endTime": "2025-02-01T11:30:00",
                "location": {
                    "latitude": 37.5665,
                    "longitude": 126.9780
                    "address": "서울특별시 중구 세종대로 110"
                }
            }
            """;

        // Perform POST 요청
        ResultActions resultActions = mvc.perform(
                        post("/schedules")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        // 기대 응답
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("해당 시간에 이미 일정이 존재합니다."));
    }




    @Test
    @DisplayName("일정 수정 성공")
    void t3() throws Exception {
        // 수정 요청 데이터 예시
        String requestBody = """
                {
                    "calendarId": 1,
                    "title": "수정된 일정 제목",
                    "description": "수정된 일정 설명",
                    "startTime": "2025-02-01T12:00:00",
                    "endTime": "2025-02-01T13:00:00",
                    "location": {
                        "latitude": 37.5678,
                        "longitude": 126.9890
                        "address": "서울특별시 중구 세종대로 110"
                    }
                }
                """;

        // 일정 ID 예시
        Long scheduleId = 1L;

        // Perform PUT 요청
        ResultActions resultActions = mvc.perform(
                        put("/schedules/{scheduleId}", scheduleId)
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        // 기대 응답
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정된 일정 제목"))
                .andExpect(jsonPath("$.description").value("수정된 일정 설명"))
                .andExpect(jsonPath("$.startTime").value("2025-02-01T12:00:00"))
                .andExpect(jsonPath("$.endTime").value("2025-02-01T13:00:00"))
                .andExpect(jsonPath("$.location.latitude").value(37.5678))
                .andExpect(jsonPath("$.location.longitude").value(126.9890))
                .andExpect(jsonPath("$.location.address").value("서울특별시 중구 세종대로 110"));
    }

    @Test
    @DisplayName("일정 수정 실패 - 일정이 존재하지 않음")
    void t4() throws Exception {
        // 요청 데이터 예시
        String requestBody = """
            {
                "calendarId": 1,
                "title": "수정된 일정 제목",
                "description": "수정된 일정 설명",
                "startTime": "2025-02-01T12:00:00",
                "endTime": "2025-02-01T13:00:00",
                "location": {
                    "latitude": 37.5678,
                    "longitude": 126.9890
                    "address": "서울특별시 중구 세종대로 110"
                }
            }
            """;

        // 존재하지 않는 일정 ID
        Long scheduleId = 999L;

        // Perform PUT 요청
        ResultActions resultActions = mvc.perform(
                        put("/schedules/{scheduleId}", scheduleId)
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        // 기대 응답
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("해당 일정을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("일정 수정 실패 - 시간 충돌")
    void t5() throws Exception {
        // 요청 데이터 예시
        String requestBody = """
            {
                "calendarId": 1,
                "title": "겹치는 시간 수정 요청",
                "description": "겹치는 일정 테스트",
                "startTime": "2025-02-01T10:00:00",
                "endTime": "2025-02-01T11:00:00",
                "location": {
                    "latitude": 37.5678,
                    "longitude": 126.9890
                    "address": "서울특별시 중구 세종대로 110"
                }
            }
            """;

        // 존재하는 일정 ID
        Long scheduleId = 1L;

        // Perform PUT 요청
        ResultActions resultActions = mvc.perform(
                        put("/schedules/{scheduleId}", scheduleId)
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        // 기대 응답
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("해당 시간에 이미 일정이 존재합니다."));
    }

    @Test
    @DisplayName("일정 삭제 성공")
    void t6() throws Exception {
        // 존재하는 일정 ID 예시
        Long scheduleId = 1L;

        // Perform DELETE 요청
        ResultActions resultActions = mvc.perform(
                        delete("/schedules/{scheduleId}", scheduleId)
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
    void t7() throws Exception {
        // 존재하지 않는 일정 ID 예시
        Long scheduleId = 999L;

        // Perform DELETE 요청
        ResultActions resultActions = mvc.perform(
                        delete("/schedules/{scheduleId}", scheduleId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        // 기대 응답
        resultActions
                .andExpect(status().isNotFound()) // 상태 코드 404
                .andExpect(jsonPath("$.message").value("해당 일정을 찾을 수 없습니다.")); // 예외 메시지 확인
    }

    @Test
    @DisplayName("일정 목록 조회 - 성공")
    void testGetSchedules() throws Exception {
        // Perform GET 요청
        ResultActions resultActions = mvc.perform(
                        get("/schedules")
                                .param("startDate", "2025-02-01")
                                .param("endDate", "2025-02-02")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        // 결과 검증
        resultActions
                .andExpect(status().isOk()) // 상태 코드 200 확인
                .andExpect(jsonPath("$.length()").value(2)) // 응답 배열 크기 검증
                .andExpect(jsonPath("$[0].title").value("회의 일정")) // 첫 번째 일정 제목 확인
                .andExpect(jsonPath("$[0].description").value("팀 회의")) // 첫 번째 일정 설명 확인
                .andExpect(jsonPath("$[0].startTime").value("2025-02-01T10:00:00")) // 첫 번째 일정 시작 시간 확인
                .andExpect(jsonPath("$[0].endTime").value("2025-02-01T11:00:00")) // 첫 번째 일정 종료 시간 확인
                .andExpect(jsonPath("$[0].location.latitude").value(37.5665)) // 첫 번째 일정 위도 확인
                .andExpect(jsonPath("$[0].location.longitude").value(126.9780)) // 첫 번째 일정 경도 확인
                .andExpect(jsonPath("$[0].location.address").value("서울특별시 중구 세종대로 110")) // 첫 번째 일정 주소 확인
                .andExpect(jsonPath("$[1].title").value("운동 일정")) // 두 번째 일정 제목 확인
                .andExpect(jsonPath("$[1].description").value("헬스장 방문")) // 두 번째 일정 설명 확인
                .andExpect(jsonPath("$[1].startTime").value("2025-02-02T15:00:00")) // 두 번째 일정 시작 시간 확인
                .andExpect(jsonPath("$[1].endTime").value("2025-02-02T16:00:00")) // 두 번째 일정 종료 시간 확인
                .andExpect(jsonPath("$[1].location.latitude").value(37.5678)) // 두 번째 일정 위도 확인
                .andExpect(jsonPath("$[1].location.longitude").value(126.9890)) // 두 번째 일정 경도 확인
                .andExpect(jsonPath("$[1].location.address").value("서울특별시 강남구 테헤란로 123")); // 두 번째 일정 주소 확인
    }


    @Test
    @DisplayName("일정 목록 조회 - 일정 없음")
    void t9() throws Exception {
        ResultActions resultActions = mvc.perform(
                        get("/schedules")
                                .param("startDate", "2025-03-01")
                                .param("endDate", "2025-03-02")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        resultActions
                .andExpect(status().isOk()) // 상태 코드 200 확인
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("특정 일정 조회 성공")
    void t10() throws Exception{
        Long scheduleId = scheduleRepository.findAll().get(0).getId();

        ResultActions resultActions = mvc.perform(
                get("/schedules/{id}", scheduleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
            )
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(scheduleId))
                .andExpect(jsonPath("$.title").value("회의 일정"))
                .andExpect(jsonPath("$.description").value("팀 회의"))
                .andExpect(jsonPath("$.startTime").value("2025-02-01T10:00:00"))
                .andExpect(jsonPath("$.endTime").value("2025-02-01T11:00:00"))
                .andExpect(jsonPath("$.location.latitude").value(37.5665))
                .andExpect(jsonPath("$.location.longitude").value(126.9780))
                .andExpect(jsonPath("$.location.address").value("서울특별시 중구 세종대로 110"));
    }

    @Test
    @DisplayName("특정 일정 조회 실패 - 일정이 존재하지 않음")
    void t11() throws Exception{
        Long scheduleId=999L;

        ResultActions resultActions = mvc.perform(
                get("/schedules/{id}", scheduleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
        ).andDo(print());

        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("해당 일정을 찾을 수 없습니다."));
    }

}
