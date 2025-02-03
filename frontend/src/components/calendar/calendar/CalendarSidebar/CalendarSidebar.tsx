// components/calendar/calendar/CalendarSidebar/CalendarSidebar.tsx
import React from "react";
import {
  CalendarIcon,
  PlusIcon,
  PencilIcon,
  TrashIcon,
} from "@heroicons/react/24/outline";
import type { Calendar } from "../../../../lib/calendar/types/calendarTypes";

interface CalendarSidebarProps {
  onCreateClick: () => void;
  onUpdateClick: () => void;
  onDeleteClick: () => void;
  onViewClick: () => void;
  selectedCalendar: Calendar | null;
}

export const CalendarSidebar: React.FC<CalendarSidebarProps> = ({
  onCreateClick,
  onUpdateClick,
  onDeleteClick,
  onViewClick,
  selectedCalendar,
}) => {
  return (
    <div className="h-screen bg-[#FFFDF9] p-4 border-r-2 border-black">
      <div className="mb-8">
        <h1 className="text-xl font-semibold text-gray-800">캘린더 관리</h1>
      </div>

      <nav className="flex flex-col space-y-4">
        <button
          onClick={onViewClick}
          className="flex items-center space-x-2 text-gray-700 hover:bg-gray-100 p-2 rounded-lg"
        >
          <CalendarIcon className="h-6 w-6" />
          <span>캘린더 조회</span>
        </button>

        <button
          onClick={onCreateClick}
          className="flex items-center space-x-2 text-gray-700 hover:bg-gray-100 p-2 rounded-lg"
        >
          <PlusIcon className="h-6 w-6" />
          <span>캘린더 생성</span>
        </button>

        <button
          onClick={onUpdateClick}
          className={`flex items-center space-x-2 text-gray-700 hover:bg-gray-100 p-2 rounded-lg ${
            !selectedCalendar && "opacity-50 cursor-not-allowed"
          }`}
          disabled={!selectedCalendar}
        >
          <PencilIcon className="h-6 w-6" />
          <span>캘린더 수정</span>
        </button>

        <button
          onClick={onDeleteClick}
          className={`flex items-center space-x-2 text-gray-700 hover:bg-gray-100 p-2 rounded-lg ${
            !selectedCalendar && "opacity-50 cursor-not-allowed"
          }`}
          disabled={!selectedCalendar}
        >
          <TrashIcon className="h-6 w-6" />
          <span>캘린더 삭제</span>
        </button>
      </nav>
    </div>
  );
};
