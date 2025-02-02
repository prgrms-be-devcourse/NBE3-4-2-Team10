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
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <form
                onSubmit={handleSubmit}
                className="p-10 bg-white rounded-lg shadow-lg"
                style={{ width: '720px', padding: '20px 28px' }}  // 폼 크기 및 패딩 조정
            >
                {/* 제목 */}
                <div className="mb-8">
                    <label className="block text-3xl font-bold mb-4">제목</label>
                    <input
                        type="text"
                        value={formData.title}
                        onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                        className="w-full border rounded px-5 py-4 text-lg"
                        required
                    />
                </div>

                {/* 시작 시간 */}
                <div className="mb-8">
                    <label className="block text-3xl font-bold mb-4">시작 시간</label>
                    <input
                        type="datetime-local"
                        value={formData.startTime}
                        onChange={(e) => setFormData({ ...formData, startTime: e.target.value })}
                        className="w-full border rounded px-5 py-4 text-lg"
                        required
                    />
                </div>

                {/* 종료 시간 */}
                <div className="mb-8">
                    <label className="block text-3xl font-bold mb-4">종료 시간</label>
                    <input
                        type="datetime-local"
                        value={formData.endTime}
                        onChange={(e) => setFormData({ ...formData, endTime: e.target.value })}
                        className="w-full border rounded px-5 py-4 text-lg"
                        required
                    />
                </div>

                {/* 주소 */}
                <div className="mb-8">
                    <label className="block text-3xl font-bold mb-4">주소</label>
                    <input
                        type="text"
                        value={formData.location.address}
                        onChange={(e) =>
                            setFormData({
                                ...formData,
                                location: { ...formData.location, address: e.target.value },
                            })
                        }
                        className="w-full border rounded px-5 py-4 text-lg"
                    />
                </div>

                {/* 설명 */}
                <div className="mb-8">
                    <label className="block text-3xl font-bold mb-4">설명</label>
                    <textarea
                        value={formData.description}
                        onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                        className="w-full border rounded px-5 py-4 text-lg"
                        rows={4}
                    />
                </div>

                {/* 버튼 */}
                <div className="flex justify-end gap-6 mt-6">
                    <button
                        type="button"
                        onClick={onCancel}
                        className="px-6 py-3 bg-gray-300 text-gray-800 text-lg font-bold rounded hover:bg-gray-400"
                    >
                        취소
                    </button>
                    <button
                        type="submit"
                        className="px-6 py-3 bg-white text-black border border-gray-300 rounded-lg hover:bg-gray-200 font-bold text-lg"
                    >
                        저장
                    </button>
                </div>
            </form>
        </div>
    );
};
