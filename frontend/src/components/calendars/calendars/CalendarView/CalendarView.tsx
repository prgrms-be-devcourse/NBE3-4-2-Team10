"use client";
import React from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import type { Calendar } from "@/lib/calendars/types/calendarTypes";
import { DateSelectArg, EventClickArg } from "@fullcalendar/core";
import client from "@/lib/backend/client";

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
    // ✅ 일정 클릭 시 해당 캘린더 선택
    const handleEventClick = (clickInfo: EventClickArg) => {
        const calendar = calendars.find(cal => String(cal.id) === clickInfo.event.id);
        if (calendar) {
            onCalendarSelect(calendar);
        }
    };

    // ✅ 날짜 선택 시 일정 추가하는 함수 (백엔드 연동 포함)
    const handleDateSelect = async (selectInfo: DateSelectArg) => {
        if (!selectedCalendar) {
            alert('먼저 캘린더를 선택해주세요.');
            return;
        }

        const title = prompt('일정 제목을 입력해주세요!');
        if (!title) return;

        let calendarApi = selectInfo.view.calendar;
        let newEvent = {
            id: String(new Date().getTime()), // 임시 ID
            title: title,
            start: selectInfo.startStr,
            allDay: true,
        };

        // 📌 1. 풀캘린더 UI에 일정 추가
        calendarApi.addEvent(newEvent);

        // 📌 2. 백엔드에 일정 저장 요청 (API 호출)
        // 스케쥴팀 참고용 : 이 코드가 백엔드의 스케쥴값과 상호작용하는 코드
        // 테스트용으로 이 코드를 주석처리해둠. 실행하면 정상적으로 캘린더에 일정 표기됨
        // 주석을 풀면 스케쥴이랑 연결이 안되서 생성은되나 오류가뜨고 새로고침시 없어짐
        // 추 후 스케쥴 완성되시면 연결하시면됩니다.
//         try {
//             const response = await fetch("/api/events", {
//                 method: "POST",
//                 headers: { "Content-Type": "application/json" },
//                 body: JSON.stringify({
//                     calendarId: selectedCalendar.id,
//                     title: title,
//                     start: selectInfo.startStr,
//                 }),
//             });
//
//             if (!response.ok) {
//                 throw new Error("서버 응답 오류");
//             }
//
//             console.log("일정 생성 성공!");
//         } catch (error) {
//             console.error("일정 추가 실패:", error);
//             alert("일정을 저장하는 중 오류가 발생했습니다.");
//         }
    };

    // ✅ 캘린더 선택 안 했을 때 안내 메시지
    if (!selectedCalendar) {
        return (
            <div className="w-full h-full flex items-center justify-center bg-white">
                <div className="text-center text-gray-500">
                    <p className="text-xl mb-2">새로운 캘린더를 만들어보세요!</p>
                    <p className="text-sm text-gray-400">좌측 메뉴에서 + NEW CALENDAR을 통해 새로운 캘린더를 만들어보세요!</p>
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
                    select={handleDateSelect} // ✅ 일정 추가 함수 연결
                    eventClick={handleEventClick}
                    handleWindowResize={true}
                    dayCellContent={(e) => e.dayNumberText}
                    height="100%"
                    aspectRatio={1.5}
                    contentHeight="auto"
                    dayMaxEventRows={true}
                    fixedWeekCount={false}
                    dayCellClassNames="min-h-[100px] p-2"
                    // Google Calendar 스타일 적용
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
        </div>
    );
};