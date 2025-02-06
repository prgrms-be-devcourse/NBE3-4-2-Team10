// src/components/calendar/calendar/CalendarView/CalendarView.tsx
"use client";
import React from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import type { Calendar } from "@/lib/calendars/types/calendarTypes";
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
    const handleEventClick = (clickInfo: EventClickArg) => {
        const calendar = calendars.find(cal => String(cal.id) === clickInfo.event.id);
        if (calendar) {
            onCalendarSelect(calendar);
        }
    };

    const handleDateSelect = (selectInfo: DateSelectArg) => {
        if (!selectedCalendar) {
            alert('먼저 캘린더를 선택해주세요.');
            return;
        }

        const title = prompt('일정 제목을 입력하세요:');
        if (!title) return;

        alert(`${selectedCalendar.name} 캘린더에 새로운 일정 "${title}"이 생성되었습니다.`);
    };

    if (!selectedCalendar) {
        return (
            <div className="w-full h-full flex items-center justify-center bg-white">
                <div className="text-center text-gray-500">
                    <p className="text-xl mb-2">캘린더를 선택해주세요</p>
                    <p className="text-sm text-gray-400">좌측에서 캘린더를 선택하여 시작하세요</p>
                </div>
            </div>
        );
    }

    return (
        <div className="w-full h-full bg-white">
            <div className="h-full p-4">
                <FullCalendar
                    plugins={[dayGridPlugin, interactionPlugin]}
                    initialView="dayGridMonth"
                    headerToolbar={{
                        left: "prev,today,next",
                        center: "title",
                        right: "dayGridMonth,dayGridWeek",
                    }}
                    events={[{
                        id: String(selectedCalendar.id),
                        title: selectedCalendar.name,
                        description: selectedCalendar.description,
                        allDay: true
                    }]}
                    selectable={true}
                    select={handleDateSelect}
                    eventClick={handleEventClick}
                    handleWindowResize={true}
                    dayCellContent={(e) => e.dayNumberText}
                    height="100%"
                    aspectRatio={1.5}
                    contentHeight="auto"
                    dayMaxEventRows={true}
                    fixedWeekCount={false}
                    dayCellClassNames="min-h-[100px] p-2"
                    // Google Calendar style customization
                    buttonText={{
                        today: '오늘',
                        month: '월',
                        week: '주',
                    }}
                    buttonIcons={{
                        prev: 'chevron-left',
                        next: 'chevron-right',
                    }}
                    views={{
                        dayGridMonth: {
                            titleFormat: { year: 'numeric', month: 'long' },
                            dayHeaderFormat: { weekday: 'short' },
                        },
                        dayGridWeek: {
                            titleFormat: { year: 'numeric', month: 'long' },
                        },
                    }}
                />
            </div>
        </div>
    );
};