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
                                                                  onClose,
                                                              }) => {
    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white rounded-lg w-2/5 max-w-xl p-6 schedule-detail-popup relative">
                {/* X 버튼 */}
                <button
                    onClick={onClose}
                    className="absolute top-2 right-2 x-button-style"
                >
                    ✕
                </button>

                <h2 className="schedule-title text-lg font-bold mb-3">{schedule.title}</h2>

                {/* 텍스트 간격 조정 */}
                <div className="schedule-detail-content space-y-6 text-sm">
                    <div className="schedule-detail-row flex items-center gap-2">
                        <span>🕒</span>
                        <p>
                            {new Date(schedule.startTime).toLocaleString()} -{' '}
                            {new Date(schedule.endTime).toLocaleString()}
                        </p>
                    </div>
                    <div className="schedule-detail-row flex items-center gap-2">
                        <span>📍</span>
                        <p>{schedule.location.address}</p>
                    </div>
                    <div className="schedule-detail-row flex items-center gap-2">
                        <span>📝</span>
                        <p>{schedule.description}</p>
                    </div>
                </div>

                <div className="schedule-detail-buttons flex justify-end mt-4 gap-4">
                    <button onClick={onEdit} className="px-4 py-2 button-style">
                        수정
                    </button>
                    <button onClick={onDelete} className="px-4 py-2 button-style">
                        삭제
                    </button>
                </div>
            </div>
        </div>
    );
};
