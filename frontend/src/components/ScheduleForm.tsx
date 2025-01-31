// src/components/ScheduleForm.tsx
"use client";

import { useState } from "react";
import { createSchedule } from "@/services/scheduleService";
import { CreateScheduleRequest } from "@/types/schedule";

const ScheduleForm = ({ calendarId }: { calendarId: number }) => {
    const [formData, setFormData] = useState<CreateScheduleRequest>({
        title: "",
        description: "",
        startTime: "",
        endTime: "",
        location: "",
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await createSchedule(calendarId, formData);
            alert("일정이 추가되었습니다!");
        } catch (error) {
            console.error("일정 추가 중 오류 발생:", error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input type="text" name="title" placeholder="제목" value={formData.title} onChange={handleChange} required />
            <textarea name="description" placeholder="설명" value={formData.description} onChange={handleChange} />
            <input type="datetime-local" name="startTime" value={formData.startTime} onChange={handleChange} required />
            <input type="datetime-local" name="endTime" value={formData.endTime} onChange={handleChange} required />
            <input type="text" name="location" placeholder="위치" value={formData.location} onChange={handleChange} />
            <button type="submit">추가</button>
        </form>
    );
};

export default ScheduleForm;