import React, { useState } from 'react';
import { Schedule, ScheduleFormData } from '../types/schedule';

interface ScheduleFormProps {
    calendarId: number;
    initialData?: Schedule;
    onSubmit: (data: ScheduleFormData) => Promise<void>;
    onCancel: () => void;
}

export const ScheduleForm: React.FC<ScheduleFormProps> = ({
                                                              initialData,
                                                              onSubmit,
                                                              onCancel
                                                          }) => {
    const [formData, setFormData] = useState<ScheduleFormData>({
        title: initialData?.title || '',
        description: initialData?.description || '',
        startTime: initialData?.startTime || '',
        endTime: initialData?.endTime || '',
        location: initialData?.location || { address: '' }
    });

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        await onSubmit(formData);
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-4 p-4">
            <div>
                <label className="block text-sm font-medium mb-1">제목</label>
                <input
                    type="text"
                    value={formData.title}
                    onChange={e => setFormData({ ...formData, title: e.target.value })}
                    className="w-full p-2 border rounded"
                    required
                />
            </div>

            <div>
                <label className="block text-sm font-medium mb-1">시작 시간</label>
                <input
                    type="datetime-local"
                    value={formData.startTime}
                    onChange={e => setFormData({ ...formData, startTime: e.target.value })}
                    className="w-full p-2 border rounded"
                    required
                />
            </div>

            <div>
                <label className="block text-sm font-medium mb-1">종료 시간</label>
                <input
                    type="datetime-local"
                    value={formData.endTime}
                    onChange={e => setFormData({ ...formData, endTime: e.target.value })}
                    className="w-full p-2 border rounded"
                    required
                />
            </div>

            <div>
                <label className="block text-sm font-medium mb-1">주소</label>
                <input
                    type="text"
                    value={formData.location.address}
                    onChange={e => setFormData({
                        ...formData,
                        location: { ...formData.location, address: e.target.value }
                    })}
                    className="w-full p-2 border rounded"
                />
            </div>

            <div>
                <label className="block text-sm font-medium mb-1">설명</label>
                <textarea
                    value={formData.description}
                    onChange={e => setFormData({ ...formData, description: e.target.value })}
                    className="w-full p-2 border rounded"
                    rows={4}
                />
            </div>

            <div className="flex justify-end space-x-2">
                <button
                    type="button"
                    onClick={onCancel}
                    className="px-4 py-2 text-gray-600 border rounded hover:bg-gray-100"
                >
                    취소
                </button>
                <button
                    type="submit"
                    className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                >
                    저장
                </button>
            </div>
        </form>
    );
};
