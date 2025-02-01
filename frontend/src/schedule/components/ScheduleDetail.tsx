import React from 'react';
import { Schedule } from '../types/schedule';

interface ScheduleDetailProps {
    schedule: Schedule;
    onEdit: () => void;
    onDelete: () => void;
    onClose: () => void;
}

export const ScheduleDetail: React.FC<ScheduleDetailProps> = ({
                                                                  schedule,
                                                                  onEdit,
                                                                  onDelete,
                                                                  onClose
                                                              }) => {
    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white rounded-lg w-1/2 max-w-xl p-6 schedule-detail-popup relative">
                {/* X 버튼 스타일 수정 */}
                <button
                    onClick={onClose}
                    className="absolute top-2 right-2 w-6 h-6 flex items-center justify-center bg-blue-500 rounded-full text-white text-sm font-bold hover:bg-blue-600"
                >
                    ✕
                </button>

                <h2 className="schedule-title">{schedule.title}</h2>

                <div className="schedule-detail-content space-y-4">
                    <div className="schedule-detail-row">
                        <span>🕒</span>
                        <p>
                            {new Date(schedule.startTime).toLocaleString()} -{' '}
                            {new Date(schedule.endTime).toLocaleString()}
                        </p>
                    </div>
                    <div className="schedule-detail-row">
                        <span>📍</span>
                        <p>{schedule.location.address}</p>
                    </div>
                    <div className="schedule-detail-row">
                        <span>📝</span>
                        <p>{schedule.description}</p>
                    </div>
                </div>

                <div className="schedule-detail-buttons flex justify-end mt-6 gap-4">
                    <button onClick={onEdit} className="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600">
                        수정
                    </button>
                    <button onClick={onDelete} className="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600">
                        삭제
                    </button>
                </div>
            </div>
        </div>
    );
};