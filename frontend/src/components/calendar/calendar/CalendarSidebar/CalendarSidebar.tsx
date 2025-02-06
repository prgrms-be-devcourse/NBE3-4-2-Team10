// src/components/calendar/calendar/CalendarSidebar/CalendarSidebar.tsx
import React from "react";
import {
    CalendarIcon,
    PlusIcon,
    PencilIcon,
    TrashIcon,
    ChevronDownIcon
} from "@heroicons/react/24/outline";
import type { Calendar } from "../../../../lib/calendar/types/calendarTypes";

interface CalendarSidebarProps {
    onCreateClick: () => void;
    onUpdateClick: () => void;
    onDeleteClick: () => void;
    onViewClick: () => void;
    selectedCalendar: Calendar | null;
    calendars?: Calendar[];
    onCalendarSelect?: (calendar: Calendar) => void;
}

export const CalendarSidebar: React.FC<CalendarSidebarProps> = ({
                                                                    onCreateClick,
                                                                    onUpdateClick,
                                                                    onDeleteClick,
                                                                    selectedCalendar,
                                                                    calendars = [],
                                                                    onCalendarSelect,
                                                                }) => {
    return (
        <div className="h-screen bg-white p-4 border-r border-gray-200">
            {/* Create Calendar Button - Google style */}
            <button
                onClick={onCreateClick}
                className="mb-8 flex items-center justify-center space-x-2 rounded-full bg-white px-6 py-3 shadow-md hover:shadow-lg border border-gray-300 w-full transition-all duration-200"
            >
                <PlusIcon className="h-5 w-5" />
                <span>새로운 캘린더</span>
            </button>

            {/* Calendar List Section */}
            <div className="mb-6">
                <div className="flex items-center justify-between mb-4 px-2">
                    <h2 className="text-sm font-medium text-gray-600">내 캘린더</h2>
                    <ChevronDownIcon className="h-4 w-4 text-gray-400" />
                </div>

                <div className="space-y-1">
                    {calendars.map((calendar) => (
                        <div
                            key={calendar.id}
                            onClick={() => onCalendarSelect?.(calendar)}
                            className={`flex items-center space-x-3 px-2 py-1.5 rounded-lg cursor-pointer text-sm ${
                                selectedCalendar?.id === calendar.id
                                    ? "bg-blue-50 text-blue-700"
                                    : "hover:bg-gray-50"
                            }`}
                        >
                            <CalendarIcon className="h-4 w-4" />
                            <span className="flex-grow truncate">{calendar.name}</span>

                            {selectedCalendar?.id === calendar.id && (
                                <div className="flex items-center space-x-1">
                                    <button
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            onUpdateClick();
                                        }}
                                        className="p-1 hover:bg-gray-100 rounded"
                                    >
                                        <PencilIcon className="h-4 w-4 text-gray-500" />
                                    </button>
                                    <button
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            onDeleteClick();
                                        }}
                                        className="p-1 hover:bg-gray-100 rounded"
                                    >
                                        <TrashIcon className="h-4 w-4 text-gray-500" />
                                    </button>
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};