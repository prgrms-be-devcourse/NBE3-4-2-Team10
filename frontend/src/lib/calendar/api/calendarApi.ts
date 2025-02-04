// lib/calendar/api/calendarApi.ts
import axios from "axios";
import type {
  Calendar,
  CalendarCreateDto,
  CalendarUpdateDto,
} from "../types/calendarTypes";

const BASE_URL = "http://localhost:8080/api/calendars";

export const calendarApi = {
  getAll: () => axios.get<Calendar[]>(BASE_URL),

  getById: (id: number) => axios.get<Calendar>(`${BASE_URL}/${id}`),

  create: (dto: CalendarCreateDto) => axios.post<Calendar>(BASE_URL, dto),

  update: (id: number, dto: CalendarUpdateDto) =>
    axios.put<Calendar>(`${BASE_URL}/${id}`, dto),

  delete: (id: number) => axios.delete(`${BASE_URL}/${id}`),
};
