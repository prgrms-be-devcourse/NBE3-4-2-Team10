//src/lib/calendar/api/calendarApi.ts
import axios from 'axios';
import type { Calendar, CalendarCreateDto, CalendarUpdateDto } from '../types/calendarTypes';

const client = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true
});

// 요청 인터셉터 추가
client.interceptors.request.use((config) => {
  // 쿠키에서 JWT 토큰을 가져와서 헤더에 추가
  const token = document.cookie
      .split('; ')
      .find(row => row.startsWith('jwtToken='))
      ?.split('=')[1];

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const calendarApi = {
  getAllCalendars: () =>
      client.get<Calendar[]>('/calendars'),

  getCalendarById: (id: number) =>
      client.get<Calendar>(`/calendars/${id}`),

  createCalendar: (data: CalendarCreateDto) =>
      client.post<Calendar>('/calendars', data),

  updateCalendar: (id: number, data: CalendarUpdateDto) =>
      client.put<Calendar>(`/calendars/${id}`, data),

  deleteCalendar: (id: number) =>
      client.delete(`/calendars/${id}`)
};