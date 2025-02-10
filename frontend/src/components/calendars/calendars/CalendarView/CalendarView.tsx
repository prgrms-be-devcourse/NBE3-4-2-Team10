"use client";
import React, { useEffect, useState } from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import type { Calendar } from "@/lib/calendars/types/calendarTypes";
import { EventClickArg } from "@fullcalendar/core";
import { scheduleApi } from "@/lib/schedule/api/scheduleApi";
import dayjs from "dayjs";
import { useRouter } from "next/navigation";

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
    const [events, setEvents] = useState<any[]>([]);
    const router = useRouter();

    useEffect(() => {
        if (!selectedCalendar) return;

        const fetchSchedules = async () => {
            try {
                const today = dayjs().format("YYYY-MM-DD");
                const fetchedSchedules = await scheduleApi.getMonthlySchedules(
                    selectedCalendar.id,
                    today
                );

                const formattedEvents = fetchedSchedules
                    .map(schedule => ({
                        id: String(schedule.id),
                        title: schedule.title,
                        start: schedule.startTime,
                        end: schedule.endTime,
                        description: schedule.description,
                        allDay: false // ✅ 시간을 유지하면서 정렬 가능하도록 설정
                    }));

                formattedEvents.sort((a, b) => dayjs(b.start).valueOf() - dayjs(a.start).valueOf()); // ⏳ 가장 늦은 일정이 위로 정렬됨

                setEvents(formattedEvents);
            } catch (error) {
                console.error("📛 일정 불러오기 실패:", error);
            }
        };

        fetchSchedules();
    }, [selectedCalendar]);

    const handleEventClick = (clickInfo: EventClickArg) => {
        const calendar = calendars.find(cal => String(cal.id) === clickInfo.event.id);
        if (calendar) {
            onCalendarSelect(calendar);
        }
    };

    return (
        <div className="w-full h-full bg-white relative">
            <div className="h-full p-4">
                <FullCalendar
                    plugins={[dayGridPlugin, interactionPlugin]}
                    initialView="dayGridMonth"
                    headerToolbar={{
                        left: "prev,today,next",
                        center: "title",
                        right: "dayGridMonth,dayGridWeek",
                    }}
                    events={events} // ✅ 백엔드에서 불러온 일정 표시
                    eventClick={handleEventClick}
                    selectable={true}
                    handleWindowResize={true}
                    dayCellContent={(e) => e.dayNumberText}
                    height="100%"
                    aspectRatio={1.5}
                    contentHeight="auto"
                    dayMaxEventRows={true}
                    fixedWeekCount={false}
                    dayCellClassNames="min-h-[100px] p-2"
                    eventDisplay="block" // ✅ 일정에 시간 숨기기
                    eventContent={(eventInfo) => (
                        <div className="truncate font-medium text-sm">{eventInfo.event.title}</div>
                    )} // ✅ 제목만 보이도록 설정
                    buttonText={{
                        today: 'TODAY',
                        month: 'M',
                        week: 'W',
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
            <button
                onClick={() => {
                    if (selectedCalendar) {
                        router.push(`/calendars/${selectedCalendar.id}/schedules`);
                    } else {
                        console.error("📛 선택된 캘린더가 없습니다.");
                    }
                }}
                className="absolute bottom-4 right-4 bg-black text-white py-2 px-4 rounded-lg shadow-md hover:bg-gray-700"
            >
                일정 페이지 이동
            </button>

        </div>
    );
};
