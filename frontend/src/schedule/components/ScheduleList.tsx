import React from 'react';
import { Schedule } from '../types/schedule';

interface ScheduleListProps {
    schedules: Schedule[];
    onSelectSchedule: (schedule: Schedule) => void;
}

export const ScheduleList: React.FC<ScheduleListProps> = ({
                                                              schedules,
                                                              onSelectSchedule,
                                                          }) => {
    return (
        <div className="schedule-list-container mx-auto max-w-6xl p-6">
            <div className="schedule-list flex flex-col gap-4">
                {schedules.length > 0 ? (
                    schedules.map((schedule) => (
                        <div
                            key={schedule.id}
                            onClick={() => onSelectSchedule(schedule)}
                            className="schedule-card p-4 bg-white rounded-lg border border-gray-300 shadow hover:shadow-md transition-shadow cursor-pointer"
                        >
                            <h3 className="text-xl font-bold text-black">{schedule.title}</h3>
                            <p className="text-black text-sm mt-2">
                                🕒 {new Date(schedule.startTime).toLocaleString()} - {new Date(schedule.endTime).toLocaleString()}
                            </p>
                            {schedule.location.address && (
                                <p className="text-black text-sm mt-1">📍 {schedule.location.address}</p>
                            )}
                        </div>
                    ))
                ) : (
                    <p className="text-gray-500 text-center">등록된 일정이 없습니다.</p>
                )}
            </div>
        </div>
    );
};
