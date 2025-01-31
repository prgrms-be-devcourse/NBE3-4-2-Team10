// src/schedules/page.tsx

import ScheduleList from "../../components/ScheduleList";
import NaverMap from "../../components/NaverMap";

export default function SchedulesPage() {
    return (
        <main>
            <h1>일정 목록</h1>

            {/* 일정 목록 */}
            <ScheduleList calendarId={1} />

            {/* 네이버 지도 */}
            <NaverMap />
        </main>
    );
}
