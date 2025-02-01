export interface Location{
    address: string;
}

export interface Schedule {
    id: number;
    calendarId: number;
    title: string;
    description: string;
    startTime: string;
    endTime: string;
    location: Location;
    createDate: string;
    modifyDate: string;
}

export interface ScheduleFormData {
    title: string;
    description: string;
    startTime: string;
    endTime: string;
    location: Location;
}