import axios from "axios";
import type {
  Calendar,
  CalendarCreateDto,
  CalendarUpdateDto,
} from "../types/calendarTypes";

const BASE_URL = "/api/calendars";

export const calendarApi = {
  // 전체 캘린더 조회
  getAll: () => axios.get<Calendar[]>(BASE_URL),

  // 특정 캘린더 조회
  getById: (id: number) => axios.get<Calendar>(`${BASE_URL}/${id}`),

  // 캘린더 생성
  create: (dto: CalendarCreateDto) => axios.post<Calendar>(BASE_URL, dto),

  // 캘린더 수정
  update: (id: number, dto: CalendarUpdateDto) =>
    axios.put<Calendar>(`${BASE_URL}/${id}`, dto),

  // 캘린더 삭제
  delete: (id: number) => axios.delete(`${BASE_URL}/${id}`),
};
