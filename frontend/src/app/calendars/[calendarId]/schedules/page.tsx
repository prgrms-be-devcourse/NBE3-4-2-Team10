'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import ScheduleForm from '@/components/ScheduleForm';
import { Schedule, ScheduleFormData } from '@/types/schedule/schedule';

export default function SchedulePage() {
    const [schedules, setSchedules] = useState<Schedule[]>([]);
    const [isFormVisible, setIsFormVisible] = useState(false);
    const [selectedSchedule, setSelectedSchedule] = useState<Schedule | undefined>(undefined);
    const router = useRouter();

    // 임시 일정 데이터를 가져오는 함수
    useEffect(() => {
        const tempSchedules: Schedule[] = [
            {
                id: 1,
                calendarId: 1,
                title: '테스트 일정 1',
                description: '테스트 설명',
                startTime: '2025-02-04T10:00:00',
                endTime: '2025-02-04T12:00:00',
                location: { address: '서울', latitude: 37.5665, longitude: 126.9780 },
                createDate: '2025-02-01T09:00:00',
                modifyDate: '2025-02-02T10:00:00',
            },
            {
                id: 2,
                calendarId: 1,
                title: '테스트 일정 2',
                description: '다른 설명',
                startTime: '2025-02-05T14:00:00',
                endTime: '2025-02-05T16:00:00',
                location: { address: '부산', latitude: 35.1796, longitude: 129.0756 },
                createDate: '2025-02-01T09:00:00',
                modifyDate: '2025-02-03T11:00:00',
            },
        ];
        setSchedules(tempSchedules);
    }, []);

    const handleCreateOrUpdateSchedule = (formData: ScheduleFormData) => {
        if (selectedSchedule) {
            // 일정 수정 로직
            const updatedSchedule: Schedule = {
                ...selectedSchedule,
                ...formData,
                modifyDate: new Date().toISOString(),
            };
            setSchedules(schedules.map(s => (s.id === updatedSchedule.id ? updatedSchedule : s)));
        } else {
            // 새 일정 추가 로직
            const newSchedule: Schedule = {
                id: schedules.length + 1,
                calendarId: 1,
                ...formData,
                createDate: new Date().toISOString(),
                modifyDate: new Date().toISOString(),
            };
            setSchedules([...schedules, newSchedule]);
        }
        setIsFormVisible(false);
        setSelectedSchedule(undefined);
    };

    return (
        <div className="max-w-4xl mx-auto p-6 bg-white text-black">
            <div className="flex justify-end mb-6">
                <button
                    onClick={() => {
                        setSelectedSchedule(undefined);
                        setIsFormVisible(true);
                    }}
                    className="p-2 bg-black text-white font-bold rounded-lg hover:bg-gray-600"
                >
                    새 일정 추가
                </button>
            </div>

            {/* 일정 생성 및 수정 폼 팝업 */}
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

            {/* 일정 목록 */}
            <div className="grid gap-4">
                {schedules.map(schedule => (
                    <div
                        key={schedule.id}
                        className="p-6 bg-gray-100 rounded-lg shadow cursor-pointer hover:bg-gray-300 transition"
                        onClick={() => {
                            router.push(`/calendars/${schedule.calendarId}/schedules/${schedule.id}`);
                        }}
                    >
                        <h2 className="text-lg font-bold flex items-center mb-2">
                            📆 {schedule.title}
                        </h2>
                        <p className="text-sm text-gray-600 flex items-center mb-1">
                            ⏱️ 시작: {new Date(schedule.startTime).toLocaleString()} ~ 종료: {new Date(schedule.endTime).toLocaleString()}
                        </p>
                        <p className="text-sm text-gray-600 flex items-center">
                            📍 {schedule.location.address}
                        </p>
                    </div>
                ))}
            </div>
        </div>
    );
}
