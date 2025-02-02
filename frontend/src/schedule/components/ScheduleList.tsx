import React from 'react';
import { Schedule } from '../types/schedule';

interface ScheduleListProps {
    schedules: Schedule[];
    onSelectSchedule: (schedule: Schedule) => void;
    onCreateSchedule: () => void;
}

export const ScheduleList: React.FC<ScheduleListProps> = ({
                                                              schedules,
                                                              onSelectSchedule,
                                                              onCreateSchedule,
                                                          }) => {
    return (
        <div className="schedule-list space-y-4 py-4">
            {schedules.map((schedule) => (
                <div
                    key={schedule.id}
                    onClick={() => onSelectSchedule(schedule)}
                    className="schedule-card w-full max-w-5xl cursor-pointer hover:shadow-lg transition-shadow"
                >
                    <h3 className="schedule-title">{schedule.title}</h3>
                    <div className="schedule-detail-row py-1">
                        <span>🕒</span>
                        <p>
                            {new Date(schedule.startTime).toLocaleString()} -{' '}
                            {new Date(schedule.endTime).toLocaleString()}
                        </p>
                    </div>
                    {schedule.location.address && (
                        <div className="schedule-detail-row py-1">
                            <span>📍</span>
                            <p>{schedule.location.address}</p>
                        </div>
                    )}
                </div>
            ))}
        </div>
    );
};
