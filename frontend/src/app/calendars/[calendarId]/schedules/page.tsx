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

    //  params가 `null`이면 기본값 할당하여 안전하게 변환
    const calendarId = params?.calendarId ? Number(params.calendarId) : null;

    useEffect(() => {
        if (calendarId === null) return; //`calendarId`가 `null`이면 실행하지 않음

        const fetchSchedules = async () => {
            try {
                const data = await scheduleApi.getSchedules(calendarId);
                setSchedules(data);
            } catch (error) {
                console.error("📛 일정 목록을 불러오는 중 오류 발생:", error);
            }
        };

        fetchSchedules();
    }, [calendarId]);

    const handleCreateOrUpdateSchedule = async (formData: ScheduleFormData) => {
        if (calendarId === null) return; // `calendarId`가 `null`이면 실행하지 않음

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
        if (calendarId === null) return; // `calendarId`가 `null`이면 실행하지 않음

        await scheduleApi.deleteSchedule(calendarId, scheduleId);
        setSchedules(schedules.filter(s => s.id !== scheduleId));
    };

    const handleViewSchedule = (scheduleId: number) => {
        console.log('Viewing schedule:', scheduleId);
    };

    // params가 `null`이면 오류 메시지 표시
    if (calendarId === null) {
        return <div className="text-center mt-20 text-xl font-bold">잘못된 접근입니다.</div>;
    }

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
