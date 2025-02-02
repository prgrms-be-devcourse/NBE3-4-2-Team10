import React, { useState } from 'react';
import { ScheduleFormData } from '../types/schedule';

interface ScheduleFormProps {
    calendarId: number;
    initialData?: ScheduleFormData;
    onSubmit: (data: ScheduleFormData) => Promise<void>;
    onCancel: () => void;
}

export const ScheduleForm: React.FC<ScheduleFormProps> = ({
                                                              initialData,
                                                              onSubmit,
                                                              onCancel,
                                                          }) => {
    const [formData, setFormData] = useState<ScheduleFormData>({
        title: initialData?.title || '',
        description: initialData?.description || '',
        startTime: initialData?.startTime || '',
        endTime: initialData?.endTime || '',
        location: initialData?.location || { address: '' },
    });

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        await onSubmit(formData);
    };

    return (
        <div className="flex items-center justify-center px-4 py-8">
            <form
                onSubmit={handleSubmit}
                className="p-8 space-y-10 bg-white rounded-lg shadow-lg w-full max-w-xl"
            >
                {/* 제목 */}
                <div className="schedule-form-row">
                    <label className="schedule-form-label">제목</label>
                    <input
                        type="text"
                        value={formData.title}
                        onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                        className="schedule-form-input"
                        required
                    />
                </div>

                {/* 시작 시간 */}
                <div className="schedule-form-row">
                    <label className="schedule-form-label">시작 시간</label>
                    <input
                        type="datetime-local"
                        value={formData.startTime}
                        onChange={(e) => setFormData({ ...formData, startTime: e.target.value })}
                        className="schedule-form-input"
                        required
                    />
                </div>

                {/* 종료 시간 */}
                <div className="schedule-form-row">
                    <label className="schedule-form-label">종료 시간</label>
                    <input
                        type="datetime-local"
                        value={formData.endTime}
                        onChange={(e) => setFormData({ ...formData, endTime: e.target.value })}
                        className="schedule-form-input"
                        required
                    />
                </div>

                {/* 주소 */}
                <div className="schedule-form-row">
                    <label className="schedule-form-label">주소</label>
                    <input
                        type="text"
                        value={formData.location.address}
                        onChange={(e) =>
                            setFormData({
                                ...formData,
                                location: { ...formData.location, address: e.target.value },
                            })
                        }
                        className="schedule-form-input"
                    />
                </div>

                {/* 설명 */}
                <div className="schedule-form-row">
                    <label className="schedule-form-label">설명</label>
                    <textarea
                        value={formData.description}
                        onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                        className="schedule-form-input"
                        rows={8}
                    />
                </div>

                {/* 버튼 */}
                <div className="flex justify-end gap-4 mt-6">
                    <button
                        type="button"
                        onClick={onCancel}
                        className="px-6 py-3 text-xl text-gray-600 border border-gray-300 rounded hover:bg-gray-100"
                    >
                        취소
                    </button>
                    <button
                        type="submit"
                        className="px-6 py-3 text-xl bg-blue-500 text-white rounded hover:bg-blue-600"
                    >
                        저장
                    </button>
                </div>
            </form>
        </div>
    );
};
