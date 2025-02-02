import React from "react";
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
    <div className="h-screen bg-white p-4 border-r border-gray-300">
      <div className="mb-8">
        <h1 className="text-xl font-semibold text-gray-800">캘린더 관리</h1>
      </div>

      <nav className="flex flex-col space-y-4">
        <button
          onClick={onViewClick}
          className="text-gray-700 hover:bg-gray-100 p-2 rounded-lg text-left"
        >
          캘린더 조회
        </button>

        <button
          onClick={onCreateClick}
          className="text-gray-700 hover:bg-gray-100 p-2 rounded-lg text-left"
        >
          캘린더 생성
        </button>

        <button
          onClick={onUpdateClick}
          className={`text-gray-700 hover:bg-gray-100 p-2 rounded-lg text-left ${
            !selectedCalendar && "opacity-50 cursor-not-allowed"
          }`}
          disabled={!selectedCalendar}
        >
          캘린더 수정
        </button>

        <button
          onClick={onDeleteClick}
          className={`text-gray-700 hover:bg-gray-100 p-2 rounded-lg text-left ${
            !selectedCalendar && "opacity-50 cursor-not-allowed"
          }`}
          disabled={!selectedCalendar}
        >
          캘린더 삭제
        </button>
      </nav>
    </div>
  );
};
