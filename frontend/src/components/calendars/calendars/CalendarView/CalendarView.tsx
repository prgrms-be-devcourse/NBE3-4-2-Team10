import React from 'react';
import { useRouter } from 'next/navigation';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import type { Calendar } from '@/lib/calendars/types/calendarTypes';
import { DateSelectArg, EventClickArg } from '@fullcalendar/core';
import './CalendarView.css';

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
    const router = useRouter();

    const handleEventClick = (clickInfo: EventClickArg) => {
        const calendar = calendars.find(cal => String(cal.id) === clickInfo.event.id);
        if (calendar) {
            onCalendarSelect(calendar);
        }
    };

    const handleDateSelect = async (selectInfo: DateSelectArg) => {
        if (!selectedCalendar) {
            alert('먼저 캘린더를 선택해주세요.');
            return;
        }

        const title = prompt('일정 제목을 입력해주세요!');
        if (!title) return;

        let calendarApi = selectInfo.view.calendar;
        let newEvent = {
            id: String(new Date().getTime()),
            title: title,
            start: selectInfo.startStr,
            allDay: true,
        };

        calendarApi.addEvent(newEvent);
    };

    if (!selectedCalendar) {
        return (
            <div className="empty-state">
                <p className="empty-state-title">새로운 캘린더를 만들어보세요!</p>
                <p className="empty-state-description">
                    좌측 메뉴에서 + NEW CALENDAR을 통해 새로운 캘린더를 만들어보세요!
                </p>
            </div>
        );
    }

    return (
        <div className="calendar-view flex flex-col h-full">
            <div className="relative flex-grow" style={{ marginBottom: '0px' }}>
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
            <div className="px-4" style={{ marginBottom: '310px'}}>
                <button
                    onClick={() => {
                        if (selectedCalendar) {
                            router.push(`/calendars/${selectedCalendar.id}/schedules`);
                        } else {
                            console.error("📛 선택된 캘린더가 없습니다.");
                        }
                    }}
                    className="float-right bg-black text-white py-2 px-4 rounded-lg shadow-md hover:bg-gray-700"
                >
                    일정 페이지 이동
                </button>
            </div>
        </div>
    );
};