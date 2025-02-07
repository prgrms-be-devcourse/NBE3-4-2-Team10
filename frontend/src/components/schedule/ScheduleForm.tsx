import React, { useState } from 'react';
import { ScheduleFormData } from '@/types/schedule/schedule';

interface ScheduleFormProps {
    initialData?: ScheduleFormData;
    onSubmit: (formData: ScheduleFormData) => void;
    onCancel: () => void;
    isNew?: boolean; // 새로운 일정 여부를 판단하는 속성 추가
}

export default function ScheduleForm({ initialData, onSubmit, onCancel, isNew }: ScheduleFormProps) {
    const [formData, setFormData] = useState<ScheduleFormData>({
        title: initialData?.title || '',
        description: initialData?.description || '',
        startTime: initialData?.startTime || '',
        endTime: initialData?.endTime || '',
        location: initialData?.location || { address: '', latitude: 0, longitude: 0 }
    });

    const handleAddressChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const address = e.target.value;
        setFormData(prev => ({ ...prev, location: { ...prev.location, address } }));
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSubmit(formData);
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-4">
            <div>
                <label className="block font-bold mb-1">제목</label>
                <input
                    type="text"
                    name="title"
                    value={formData.title}
                    onChange={handleChange}
                    className="w-full p-2 border rounded"
                    required
                />
            </div>

            <div>
                <label className="block font-bold mb-1">시간</label>
                <input
                    type="datetime-local"
                    name="startTime"
                    value={formData.startTime}
                    onChange={handleChange}
                    className="w-full p-2 border rounded mb-2"
                    required
                />
                <input
                    type="datetime-local"
                    name="endTime"
                    value={formData.endTime}
                    onChange={handleChange}
                    className="w-full p-2 border rounded"
                    required
                />
            </div>

            <div>
                <label className="block font-bold mb-1">주소</label>
                <input
                    type="text"
                    name="location.address"
                    value={formData.location.address}
                    onChange={handleAddressChange}
                    className="w-full p-2 border rounded"
                />
            </div>

            <div>
                <label className="block font-bold mb-1">설명</label>
                <textarea
                    name="description"
                    value={formData.description}
                    onChange={handleChange}
                    className="w-full p-2 border rounded"
                />
            </div>

            <div className="flex justify-end space-x-4">
                <button type="submit" className="p-2 bg-black text-white font-bold rounded hover:bg-gray-700">
                    {isNew ? '저장' : '수정'}
                </button>
                <button type="button" onClick={onCancel} className="p-2 bg-black text-white font-bold rounded hover:bg-gray-400">
                    취소
                </button>
            </div>
        </form>
    );
}