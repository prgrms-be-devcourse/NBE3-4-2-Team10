// components/calendar/calendar/CalendarView/CalendarView.tsx
"use client";
import React from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import type { Calendar } from "../../../../lib/calendar/types/calendarTypes";
import { useCalendar } from "../../../../lib/calendar/hooks/useCalendar";
import { DateSelectArg, EventClickArg } from "@fullcalendar/core";

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
  const { createCalendar, deleteCalendar } = useCalendar();

  const handleDateSelect = async (selectInfo: DateSelectArg) => {
    const title = prompt("일정 제목을 입력하세요:");
    if (title) {
      const calendarName = title;
      const description = prompt("일정 설명을 입력하세요:") || "";

      try {
        await createCalendar({ name: calendarName, description });
        selectInfo.view.calendar.unselect();
      } catch (err) {
        alert("일정 생성에 실패했습니다.");
        console.error(err);
      }
    }
  };

  const handleEventClick = async (clickInfo: EventClickArg) => {
    if (
      selectedCalendar &&
      confirm(`'${clickInfo.event.title}' 일정을 삭제하시겠습니까?`)
    ) {
      try {
        await deleteCalendar(selectedCalendar.id);
      } catch (err) {
        alert("일정 삭제에 실패했습니다.");
        console.error(err);
      }
    }
  };

  return (
    <div className="w-full p-4 bg-white">
      <div style={{ height: "calc(100vh - 2rem)" }}>
        <FullCalendar
          plugins={[dayGridPlugin, interactionPlugin]}
          initialView="dayGridMonth"
          headerToolbar={{
            left: "prev,today,next",
            center: "title",
            right: "dayGridMonth,dayGridWeek",
          }}
          events={calendars.map((calendar) => ({
            id: String(calendar.id),
            title: calendar.name,
            description: calendar.description,
          }))}
          selectable={true}
          select={handleDateSelect}
          eventClick={handleEventClick}
          handleWindowResize={true}
          dayCellContent={(e) => {
            return e.dayNumberText;
          }}
          views={{
            dayGridMonth: {
              titleFormat: { year: "numeric", month: "long" },
              dayHeaderFormat: { weekday: "short" },
              dayPopoverFormat: {
                month: "long",
                day: "numeric",
                year: "numeric",
              },
            },
          }}
          // 달력 크기 관련 설정
          height="100%"
          aspectRatio={1.5}
          // 스타일 관련 설정
          contentHeight="auto"
          dayMaxEventRows={true}
          fixedWeekCount={false}
          // 달력 셀 스타일을 위한 추가 설정
          dayCellClassNames="min-h-[100px] p-2"
        />
      </div>
    </div>
  );
};
