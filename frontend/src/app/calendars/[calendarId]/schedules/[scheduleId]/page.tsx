'use client';

import { useState, useEffect } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { Schedule, ScheduleFormData } from '@/types/schedule';
import ScheduleForm from '@/components/ScheduleForm';

export default function ScheduleDetailPage() {
    const params = useParams();
    const router = useRouter();
    const calendarId = Number(params.calendarId);
    const scheduleId = Number(params.scheduleId);

    const [schedule, setSchedule] = useState<Schedule | null>(null);
    const [isEditFormVisible, setIsEditFormVisible] = useState(false);

    useEffect(() => {
        const fetchSchedule = async () => {
            const fetchedSchedule: Schedule = {
                id: scheduleId,
                calendarId,
                title: '테스트 일정 1',
                description: '테스트 설명입니다.',
                startTime: '2025-02-04T10:00:00',
                endTime: '2025-02-04T12:00:00',
                location: { address: '서울', latitude: 37.5665, longitude: 126.9780 },
                createDate: '2025-02-01T09:00:00',
                modifyDate: '2025-02-02T10:00:00',
            };
            setSchedule(fetchedSchedule);
        };

        fetchSchedule();
    }, [calendarId, scheduleId]);

    const handleUpdateSchedule = (formData: ScheduleFormData) => {
        setSchedule(prev => prev ? { ...prev, ...formData } : null);
        setIsEditFormVisible(false);
        alert('일정이 수정되었습니다.');
    };

    if (!schedule) {
        return <div className="text-center mt-20 text-xl font-bold">Loading...</div>;
    }

    return (
        <div className="max-w-4xl mx-auto p-8 bg-white text-black rounded-lg shadow-lg mt-10">
            <h1 className="text-3xl font-bold mb-6 flex items-center">
                📆 {schedule.title}
            </h1>
            <div className="mb-4 flex items-center">
                <span className="text-lg font-semibold mr-2">⏱️ 시작:</span>
                <span className="text-lg">{new Date(schedule.startTime).toLocaleString()}</span>
            </div>
            <div className="mb-4 flex items-center">
                <span className="text-lg font-semibold mr-2">⏱️ 종료:</span>
                <span className="text-lg">{new Date(schedule.endTime).toLocaleString()}</span>
            </div>
            <div className="mb-4 flex items-center">
                <span className="text-lg font-semibold mr-2">📍 위치:</span>
                <span className="text-lg">{schedule.location.address}</span>
            </div>
            <div className="mb-4 flex items-center">
                <span className="text-lg font-semibold mr-2">📝 설명:</span>
                <span className="text-lg">{schedule.description}</span>
            </div>

            <div className="flex justify-center mt-8 gap-4">
                <button
                    onClick={() => router.push(`/calendars/${calendarId}/schedules`)}
                    className="px-6 py-2 bg-black text-white font-bold rounded-lg hover:bg-gray-700"
                >
                    목록
                </button>
                <button
                    onClick={() => setIsEditFormVisible(true)}
                    className="px-6 py-2 bg-black text-white font-bold rounded-lg hover:bg-gray-700"
                >
                    수정
                </button>
            </div>

            {/* 수정 폼 팝업 */}
            {isEditFormVisible && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white p-6 rounded-lg shadow-lg max-w-xl w-full">
                        <ScheduleForm
                            initialData={schedule}
                            onSubmit={handleUpdateSchedule}
                            onCancel={() => setIsEditFormVisible(false)}
                        />
                    </div>
                </div>
            )}
        </div>
    );
}