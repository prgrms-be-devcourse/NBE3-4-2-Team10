"use client";
import React from "react";
import {
  CalendarIcon,
  PlusIcon,
  PencilIcon,
  TrashIcon,
} from "@heroicons/react/24/outline";
import type { Calendar } from "@/lib/calendar/types/calendarTypes";

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
    <div className="h-full bg-white shadow-lg">
      <div className="p-4">
        <h2 className="text-xl font-semibold text-gray-800 mb-6">
          캘린더 관리
        </h2>
        <nav className="flex flex-col items-center space-y-8">
          <button
            onClick={onViewClick}
            className="flex flex-col items-center w-full"
          >
            <CalendarIcon className="w-8 h-8 mb-2" />
            <span className="text-sm">캘린더 조회</span>
          </button>

          <button
            onClick={onCreateClick}
            className="flex flex-col items-center w-full"
          >
            <PlusIcon className="w-8 h-8 mb-2" />
            <span className="text-sm">캘린더 생성</span>
          </button>

          <button
            onClick={onUpdateClick}
            disabled={!selectedCalendar}
            className="flex flex-col items-center w-full"
          >
            <PencilIcon className="w-8 h-8 mb-2" />
            <span className="text-sm">캘린더 수정</span>
          </button>

          <button
            onClick={onDeleteClick}
            disabled={!selectedCalendar}
            className="flex flex-col items-center w-full"
          >
            <TrashIcon className="w-8 h-8 mb-2" />
            <span className="text-sm">캘린더 삭제</span>
          </button>
        </nav>
      </div>
    </div>
  );
};

interface SidebarButtonProps {
  icon: React.ElementType;
  text: string;
  onClick: () => void;
  disabled?: boolean;
}

const SidebarButton: React.FC<SidebarButtonProps> = ({
  icon: Icon,
  text,
  onClick,
  disabled = false,
}) => {
  return (
    <button
      onClick={onClick}
      disabled={disabled}
      className={`
       flex flex-col items-center justify-center 
       w-full p-4 mb-4
       text-gray-700 hover:bg-gray-100
       rounded-lg transition-colors
       ${disabled ? "opacity-50 cursor-not-allowed" : ""}
     `}
    >
      <Icon className="w-8 h-8" />
      <span className="mt-2 text-sm">{text}</span>
    </button>
  );
};
