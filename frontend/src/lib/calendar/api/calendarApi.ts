// lib/calendar/api/calendarApi.ts
import axios from "axios";
import type {
  Calendar,
  CalendarCreateDto,
  CalendarUpdateDto,
} from "../types/calendarTypes";

// 백엔드 서버 URL을 명시적으로 지정
const BASE_URL = "http://localhost:8080/api/calendars";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true, // CORS 요청에 쿠키 포함
});

export const calendarApi = {
  getAll: () => axiosInstance.get<Calendar[]>("/api/calendars"),

  getById: (id: number) => axiosInstance.get<Calendar>(`/api/calendars/${id}`),

  create: (dto: CalendarCreateDto) =>
      axiosInstance.post<Calendar>("/api/calendars", dto),

  update: (id: number, dto: CalendarUpdateDto) =>
      axiosInstance.put<Calendar>(`/api/calendars/${id}`, dto),

  delete: (id: number) => axiosInstance.delete(`/api/calendars/${id}`),
};