"use client";
import React from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import type { Calendar } from "@/lib/calendar/types/calendarTypes";

interface CalendarViewProps {
  calendars: Calendar[];
  selectedCalendar: Calendar | null;
  onCalendarSelect: (calendar: Calendar) => void;
}

export const CalendarView: React.FC<CalendarViewProps> = ({
  calendars,
  selectedCalendar,
  onCalendarSelect,
}) => {
  // 캘린더 목록을 사이드 패널로 표시
  const renderCalendarList = () => {
    return (
      <div className="mb-6">
        <h3 className="font-semibold mb-2">내 캘린더 목록</h3>
        <div className="space-y-2">
          {calendars.map((calendar) => (
            <div
              key={calendar.id}
              className={`p-2 rounded cursor-pointer ${
                selectedCalendar?.id === calendar.id
                  ? "bg-blue-100 text-blue-800"
                  : "hover:bg-gray-100"
              }`}
              onClick={() => onCalendarSelect(calendar)}
            >
              <div className="font-medium">{calendar.name}</div>
              {calendar.description && (
                <div className="text-sm text-gray-600">
                  {calendar.description}
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
    );
  };

  return (
    <div className="flex-1 p-8">
      <div className="flex space-x-6">
        {/* 캘린더 목록 */}
        <div className="w-64 bg-white rounded-lg shadow p-4">
          {renderCalendarList()}
        </div>

        {/* 캘린더 뷰 */}
        <div className="flex-1 bg-white rounded-lg shadow p-6">
          <FullCalendar
            plugins={[dayGridPlugin, interactionPlugin]}
            initialView="dayGridMonth"
            headerToolbar={{
              left: "prev,next today",
              center: "title",
              right: "dayGridMonth,dayGridWeek",
            }}
            height="80vh"
            // 추가적인 FullCalendar 설정들을 여기에 추가할 수 있습니다
          />
        </div>
      </div>
    </div>
  );
};
