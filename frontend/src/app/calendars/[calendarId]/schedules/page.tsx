'use client';

import { useState, useEffect } from 'react';
import { useParams } from 'next/navigation';
import ScheduleForm from '@/components/schedule/ScheduleForm';
import ScheduleList from '@/components/schedule/ScheduleList';
import { Schedule, ScheduleFormData } from '@/types/schedule/schedule';
import { scheduleApi } from '@/lib/schedule/api/scheduleApi';
import DynamicMapWithMarkers from '@/components/schedule/DynamicMapWithMarkers';

export default function SchedulePage() {
    const [schedules, setSchedules] = useState<Schedule[]>([]);
    const [isFormVisible, setIsFormVisible] = useState(false);
    const [selectedSchedule, setSelectedSchedule] = useState<Schedule | undefined>(undefined);
    const [selectedMarkerId, setSelectedMarkerId] = useState<number | undefined>(undefined);
    const [selectedDate, setSelectedDate] = useState<string | null>(null); // 초기값을 null로 설정

    const params = useParams();
    const calendarId = params?.calendarId ? Number(params.calendarId) : null;

    useEffect(() => {
        const storedDate = localStorage.getItem("selectedDate");
        if (storedDate) {
            setSelectedDate(storedDate);
        } else {
            setSelectedDate(new Date().toISOString().split("T")[0]); // 로컬스토리지에 값이 없으면 오늘 날짜 설정
        }
    }, []);

    useEffect(() => {
        if (selectedDate !== null) {
            localStorage.setItem("selectedDate", selectedDate);
        }
    }, [selectedDate]);

    useEffect(() => {
        if (calendarId === null || selectedDate === null) return;

        console.log("Fetching schedules for calendarId:", calendarId);
        console.log("Selected date:", selectedDate);

        const fetchSchedules = async () => {
            try {
                const data = await scheduleApi.getSchedules(calendarId, selectedDate);
                console.log("Fetched schedules:", data);
                setSchedules(data);
            } catch (error) {
                console.error("Error fetching schedules:", error);
            }
        };

        fetchSchedules();
    }, [calendarId, selectedDate]);

    const handleCreateOrUpdateSchedule = async (formData: ScheduleFormData) => {
        if (calendarId === null) return;

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
        if (calendarId === null) return;

        await scheduleApi.deleteSchedule(calendarId, scheduleId);
        setSchedules(schedules.filter(s => s.id !== scheduleId));
    };

    const handleViewSchedule = (scheduleId: number) => {
        setSelectedMarkerId(scheduleId); // 선택한 일정의 마커로 지도 이동
    };

    const sortedSchedules = [...schedules].sort((a, b) =>
        new Date(a.startTime).getTime() - new Date(b.startTime).getTime()
    );

    // 마커의 번호를 초기 순서대로 유지
    const markers = sortedSchedules
        .filter(schedule => schedule.location)
        .map((schedule, index) => ({
            id: schedule.id,
            lat: schedule.location.latitude,
            lng: schedule.location.longitude,
            address: schedule.location.address,
            label: `${index + 1}`, // 고정된 마커 번호 유지
        }));

    if (calendarId === null) {
        return <div className="text-center mt-20 text-xl font-bold">Invalid access.</div>;
    }

    return (
        <div className="max-w-4xl mx-auto p-6 bg-white text-black">
            <div className="flex justify-between mb-6">
                <input
                    type="date"
                    value={selectedDate || ""}
                    onChange={(e) => setSelectedDate(e.target.value)}
                    className="border p-2 rounded"
                />
                <button onClick={() => setIsFormVisible(true)} className="p-2 bg-black text-white rounded-lg">
                    새 일정 추가
                </button>
            </div>

            <DynamicMapWithMarkers markers={markers} selectedMarkerId={selectedMarkerId} />

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

            {sortedSchedules.length === 0 ? (
                <div className="text-center text-gray-500 mt-4">해당 날짜에 일정이 없습니다.</div>
            ) : (
                <ScheduleList
                    schedules={sortedSchedules}
                    onEdit={setSelectedSchedule}
                    onDelete={handleDeleteSchedule}
                    onView={handleViewSchedule} // 버튼 클릭 시 지도 위치만 이동
                />
            )}
        </div>
    );
}
