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
            <div className="bg-white rounded-lg w-3/5 max-w-2xl p-8 schedule-detail-popup relative">
                {/* X 버튼 */}
                <button
                    onClick={onClose}
                    className="absolute top-[-10px] right-[-10px] text-xl x-button-style"
                >
                    ✕
                </button>

                <h2 className="schedule-title text-4xl font-bold mb-6">{schedule.title}</h2>

                {/* 텍스트 간격 조정 */}
                <div className="schedule-detail-content space-y-6 text-lg">
                    <div className="schedule-detail-row flex items-center gap-3 mb-4">
                        <span>🕒</span>
                        <p>
                            {new Date(schedule.startTime).toLocaleString()} -{' '}
                            {new Date(schedule.endTime).toLocaleString()}
                        </p>
                    </div>
                    <div className="schedule-detail-row flex items-center gap-3 mb-4">
                        <span>📍</span>
                        <p>{schedule.location.address}</p>
                    </div>
                    <div className="schedule-detail-row flex items-center gap-3 mb-4">
                        <span>📝</span>
                        <p>{schedule.description}</p>
                    </div>
                </div>

                <div className="schedule-detail-buttons flex justify-end mt-10 gap-6">
                    <button
                        onClick={onEdit}
                        className="px-6 py-3 text-lg font-bold bg-white text-black rounded-lg hover:bg-gray-200"
                    >
                        수정
                    </button>
                    <button
                        onClick={onDelete}
                        className="px-6 py-3 text-lg font-bold bg-white text-black rounded-lg hover:bg-gray-200"
                    >
                        삭제
                    </button>
                </div>
            </div>
        </div>
    );
};
