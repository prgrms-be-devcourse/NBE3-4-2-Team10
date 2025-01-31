// src/components/ScheduleList.tsx
"use client";

import { useEffect, useState } from "react";
import { Schedule } from "@/types/schedule";
import { fetchSchedules, deleteSchedule } from "@/services/scheduleService";

const ScheduleList = ({ calendarId }: { calendarId: number }) => {
    const [schedules, setSchedules] = useState<Schedule[]>([]);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        const loadSchedules = async () => {
            setLoading(true);
            try {
                const data = await fetchSchedules(calendarId, "2025-02-01", "2025-02-10");
                setSchedules(data);
            } catch (error) {
                console.error("일정을 불러오는 중 오류 발생:", error);
            }
            setLoading(false);
        };
        loadSchedules();
    }, [calendarId]);

    const handleDelete = async (scheduleId: number) => {
        if (confirm("일정을 삭제하시겠습니까?")) {
            await deleteSchedule(calendarId, scheduleId);
            setSchedules(schedules.filter(schedule => schedule.id !== scheduleId));
        }
    };

    if (loading) return <p>로딩 중...</p>;

    return (
        <div>
            <h2>일정 목록</h2>
            <ul>
                {schedules.map((schedule) => (
                    <li key={schedule.id}>
                        <strong>{schedule.title}</strong> ({schedule.startTime} - {schedule.endTime})
                        <button onClick={() => handleDelete(schedule.id)}>삭제</button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ScheduleList;