import { Schedule, ScheduleFormData } from '../types/schedule';

export const scheduleApi = {
    getSchedules: async (calendarId: number, startDate: string, endDate: string): Promise<Schedule[]> => {
        console.log(`Fetching schedules for calendarId: ${calendarId}, startDate: ${startDate}, endDate: ${endDate}`);

        return [
            {
                id: 1,
                calendarId: 1,
                title: 'Test Schedule 1',
                description: 'This is the first test schedule.',
                startTime: new Date().toISOString(),
                endTime: new Date(new Date().getTime() + 3600000).toISOString(), // 현재 시간 + 1시간
                location: { address: 'Test Address 1' },
                createDate: new Date().toISOString(),
                modifyDate: new Date().toISOString(),
            },
            {
                id: 2,
                calendarId: 1,
                title: 'Test Schedule 2',
                description: 'This is the second test schedule.',
                startTime: new Date().toISOString(),
                endTime: new Date(new Date().getTime() + 7200000).toISOString(), // 현재 시간 + 2시간
                location: { address: 'Test Address 2' },
                createDate: new Date().toISOString(),
                modifyDate: new Date().toISOString(),
            },
        ];
    },

    getScheduleById: async (calendarId: number, scheduleId: number): Promise<Schedule> => {
        console.log(`Fetching schedule detail (dummy data) for scheduleId: ${scheduleId}`);
        return {
            id: scheduleId,
            calendarId,
            title: `Detailed Schedule ${scheduleId}`,
            description: `Detailed description for schedule ${scheduleId}.`,
            startTime: new Date().toISOString(),
            endTime: new Date(new Date().getTime() + 3600000).toISOString(), // 현재 시간 + 1시간
            location: { address: `Detailed Test Address ${scheduleId}` },
            createDate: new Date().toISOString(),
            modifyDate: new Date().toISOString(),
        };
    },

    createSchedule: async (calendarId: number, data: ScheduleFormData): Promise<void> => {
        console.log(`Creating schedule (dummy) in calendar ${calendarId}`, data);
    },

    updateSchedule: async (calendarId: number, scheduleId: number, data: ScheduleFormData): Promise<void> => {
        console.log(`Updating schedule (dummy) ${scheduleId} in calendar ${calendarId}`, data);
    },

    deleteSchedule: async (calendarId: number, scheduleId: number): Promise<void> => {
        console.log(`Deleting schedule (dummy) ${scheduleId} in calendar ${calendarId}`);
    },
};

// import axios from 'axios';


// import { Schedule, ScheduleFormData } from '../types/schedule';
//
// const BASE_URL = 'const BASE_URL = \'http://localhost:8080/calendars\';\n';
//
// export const scheduleApi = {
//     createSchedule: async (calendarId: number, data: ScheduleFormData): Promise<Schedule> => {
//         const response = await axios.post<Schedule>(`${BASE_URL}/${calendarId}/schedules`, data);
//         return response.data;
//     },
//
//     updateSchedule: async (calendarId: number, scheduleId: number, data: ScheduleFormData): Promise<Schedule> => {
//         const response = await axios.put<Schedule>(`${BASE_URL}/${calendarId}/schedules/${scheduleId}`, data);
//         return response.data;
//     },
//
//     deleteSchedule: async (calendarId: number, scheduleId: number): Promise<void> => {
//         await axios.delete(`${BASE_URL}/${calendarId}/schedules/${scheduleId}`);
//     },
//
//     getSchedules: async (calendarId: number, startDate: string, endDate: string): Promise<Schedule[]> => {
//         const response = await axios.get<Schedule[]>(`${BASE_URL}/${calendarId}/schedules`, {
//             params: { startDate, endDate }
//         });
//         return response.data;
//     },
//
//     getScheduleById: async (calendarId: number, scheduleId: number): Promise<Schedule> => {
//         const response = await axios.get<Schedule>(`${BASE_URL}/${calendarId}/schedules/${scheduleId}`);
//         return response.data;
//     }
// };

