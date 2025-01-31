// src/services/scheduleService.ts

import axios from "axios";
import { Schedule, CreateScheduleRequest } from "../types/schedule";

const API_BASE_URL = "http://localhost:8080/calendars"; // 백엔드 API URL

export const fetchSchedules = async (calendarId: number, startDate: string, endDate: string): Promise<Schedule[]> => {
    const response = await axios.get(`${API_BASE_URL}/${calendarId}/schedules`, {
        params: { startDate, endDate }
    });
    return response.data;
};

export const createSchedule = async (calendarId: number, scheduleData: CreateScheduleRequest): Promise<Schedule> => {
    const response = await axios.post(`${API_BASE_URL}/${calendarId}/schedules`, scheduleData);
    return response.data;
};

export const deleteSchedule = async (calendarId: number, scheduleId: number): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/${calendarId}/schedules/${scheduleId}`);
};
