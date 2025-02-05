//src/lib/calendar/api.ts
import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/calendars"; // 백엔드 URL

export const getAllCalendars = async () => {
  const response = await axios.get(`${API_BASE_URL}`);
  return response.data;
};

export const getCalendarById = async (id) => {
  const response = await axios.get(`${API_BASE_URL}/${id}`);
  return response.data;
};

export const createCalendar = async (calendarData) => {
  const response = await axios.post(`${API_BASE_URL}`, calendarData);
  return response.data;
};

export const updateCalendar = async (id, calendarData) => {
  const response = await axios.put(`${API_BASE_URL}/${id}`, calendarData);
  return response.data;
};

export const deleteCalendar = async (id) => {
  const response = await axios.delete(`${API_BASE_URL}/${id}`);
  return response.data;
};