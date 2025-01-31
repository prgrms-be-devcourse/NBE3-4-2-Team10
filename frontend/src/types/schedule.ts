// src/types/schedule.ts

import { Location } from "./location";

export type Schedule = {
    id: number;
    calendarId: number;
    title: string;
    description?: string;
    startTime: string;
    endTime: string;
    location?: Location; // Location 타입 적용
    createDate: string;
    modifyDate: string;
};

export type CreateScheduleRequest = {
    title: string;
    description?: string;
    startTime: string;
    endTime: string;
    location?: string;
};
