'use client';

import { useParams } from 'next/navigation';  // useParams를 import
import { useState, useEffect } from 'react';
import { Schedule, ScheduleFormData } from '@/schedule/types/schedule';
import { scheduleApi } from '@/schedule/services/scheduleApi';
import { ScheduleForm } from '@/schedule/components/ScheduleForm';
import { ScheduleList } from '@/schedule/components/ScheduleList';
import { ScheduleDetail } from '@/schedule/components/ScheduleDetail';

export default function CalendarPage() {
    const params = useParams();
    const calendarId = params.calendarId ? Number(params.calendarId) : null;

    const [schedules, setSchedules] = useState<Schedule[]>([]);
    const [selectedSchedule, setSelectedSchedule] = useState<Schedule | null>(null);
    const [isFormOpen, setIsFormOpen] = useState(false);
    const [isEditing, setIsEditing] = useState(false);

    useEffect(() => {
        if (calendarId) {
            loadSchedules();
        }
    }, [calendarId]);

    const loadSchedules = async () => {
        if (!calendarId) return;

        const startDate = new Date();
        const endDate = new Date();
        endDate.setMonth(endDate.getMonth() + 1);

        const data = await scheduleApi.getSchedules(
            calendarId,
            startDate.toISOString().split('T')[0],
            endDate.toISOString().split('T')[0]
        );
        setSchedules(data);
    };

    const handleCreateSchedule = async (data: ScheduleFormData) => {
        if (!calendarId) return;
        await scheduleApi.createSchedule(calendarId, data);
        setIsFormOpen(false);
        loadSchedules();
    };

    const handleUpdateSchedule = async (data: ScheduleFormData) => {
        if (!calendarId || !selectedSchedule) return;
        await scheduleApi.updateSchedule(calendarId, selectedSchedule.id, data);
        setIsEditing(false);
        setSelectedSchedule(null);
        loadSchedules();
    };

    const handleDeleteSchedule = async () => {
        if (!calendarId || !selectedSchedule) return;
        await scheduleApi.deleteSchedule(calendarId, selectedSchedule.id);
        setSelectedSchedule(null);
        loadSchedules();
    };

    return (
        <div className="max-w-6xl mx-auto px-2 pt-8">
            <div className="flex justify-between items-center mb-8">
                <h1 className="text-4xl font-bold">일정 목록</h1>
                <button
                    onClick={() => setIsFormOpen(true)}
                    className="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 text-lg font-medium ml-8"
                >
                    새 일정
                </button>
            </div>

            {calendarId && isFormOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white rounded-lg w-full max-w-6xl">
                        <ScheduleForm
                            calendarId={calendarId}
                            onSubmit={handleCreateSchedule}
                            onCancel={() => setIsFormOpen(false)}
                        />
                    </div>
                </div>
            )}

            {calendarId && isEditing && selectedSchedule && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white rounded-lg w-full max-w-md">
                        <ScheduleForm
                            calendarId={calendarId}
                            initialData={selectedSchedule}
                            onSubmit={handleUpdateSchedule}
                            onCancel={() => setIsEditing(false)}
                        />
                    </div>
                </div>
            )}

            {calendarId && selectedSchedule && !isEditing && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white rounded-lg w-full max-w-md">
                        <ScheduleDetail
                            schedule={selectedSchedule}
                            onEdit={() => setIsEditing(true)}
                            onDelete={handleDeleteSchedule}
                            onClose={() => setSelectedSchedule(null)}
                        />
                    </div>
                </div>
            )}

            {calendarId && (
                <ScheduleList
                    schedules={schedules}
                    onSelectSchedule={setSelectedSchedule}
                    onCreateSchedule={() => setIsFormOpen(true)}
                />
            )}
        </div>
    );
}