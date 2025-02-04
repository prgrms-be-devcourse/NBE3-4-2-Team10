import axios from 'axios';
import { Schedule, ScheduleFormData } from '../types/schedule';

const BASE_URL = 'http://localhost:8080/calendars';

export const scheduleApi = {
    async createSchedule(calendarId: number, scheduleData: ScheduleFormData): Promise<Schedule> {
        const response = await axios.post<Schedule>(`${BASE_URL}/${calendarId}/schedules`, scheduleData);
        return response.data;
    },

    async updateSchedule(calendarId: number, scheduleId: number, scheduleData: ScheduleFormData): Promise<Schedule> {
        const response = await axios.put<Schedule>(`${BASE_URL}/${calendarId}/schedules/${scheduleId}`, scheduleData);
        return response.data;
    },

    async deleteSchedule(calendarId: number, scheduleId: number): Promise<void> {
        await axios.delete(`${BASE_URL}/${calendarId}/schedules/${scheduleId}`);
    },

    async getSchedules(calendarId: number, startDate: string, endDate: string): Promise<Schedule[]> {
        const response = await axios.get<Schedule[]>(`${BASE_URL}/${calendarId}/schedules`, {
            params: { startDate, endDate },
        });
        return response.data;
    },

    async getScheduleById(calendarId: number, scheduleId: number): Promise<Schedule> {
        const response = await axios.get<Schedule>(`${BASE_URL}/${calendarId}/schedules/${scheduleId}`);
        return response.data;
    },
};
