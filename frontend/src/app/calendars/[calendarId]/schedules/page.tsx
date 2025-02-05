'use client';

import { useState, useEffect } from 'react';
import { useParams } from 'next/navigation';
import ScheduleForm from '@/components/schedule/ScheduleForm';
import ScheduleList from '@/components/schedule/ScheduleList';
import { Schedule, ScheduleFormData } from '@/types/schedule/schedule';
import { scheduleApi } from '@/lib/schedule/api/scheduleApi';

export default function SchedulePage() {
    const [schedules, setSchedules] = useState<Schedule[]>([]);
    const [isFormVisible, setIsFormVisible] = useState(false);
    const [selectedSchedule, setSelectedSchedule] = useState<Schedule | undefined>(undefined);
    const params = useParams();

    // 동적으로 calendarId 가져오기
    const calendarId = Number(params.calendarId);

    useEffect(() => {
        if (!calendarId) return;

        const fetchSchedules = async () => {
            const data = await scheduleApi.getSchedules(calendarId);
            setSchedules(data);
        };

        fetchSchedules();
    }, [calendarId]);

    const handleCreateOrUpdateSchedule = async (formData: ScheduleFormData) => {
        if (selectedSchedule) {
            await scheduleApi.updateSchedule(calendarId, selectedSchedule.id, formData);
            setSchedules(schedules.map(s => (s.id === selectedSchedule.id ? { ...s, ...formData } : s)));
        } else {
            const newSchedule = await scheduleApi.createSchedule(calendarId, formData);
            setSchedules([...schedules, newSchedule]);
        }
        setIsFormVisible(false);
    };

    const handleDeleteSchedule = async (scheduleId: number) => {
        await scheduleApi.deleteSchedule(calendarId, scheduleId);
        setSchedules(schedules.filter(s => s.id !== scheduleId));
    };

    const handleViewSchedule = (scheduleId: number) => {
        // 여기서 원하는 경로로 이동하거나 특정 동작 수행
        console.log('Viewing schedule:', scheduleId);
    };

    return (
        <div className="max-w-4xl mx-auto p-6 bg-white text-black">
            <div className="flex justify-end mb-6">
                <button onClick={() => setIsFormVisible(true)} className="p-2 bg-black text-white rounded-lg">
                    새 일정 추가
                </button>
            </div>

            {isFormVisible && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white p-6 rounded-lg shadow-lg max-w-xl w-full">
                        <ScheduleForm
                            initialData={selectedSchedule}
                            onSubmit={handleCreateOrUpdateSchedule}
                            onCancel={() => setIsFormVisible(false)}
                        />
                    </div>
                </div>
            )}

            <ScheduleList
                schedules={schedules}
                onEdit={setSelectedSchedule}
                onDelete={handleDeleteSchedule}
                onView={handleViewSchedule}
            />
        </div>
    );
}
