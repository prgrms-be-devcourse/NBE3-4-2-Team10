//src/lib/calendar/hooks/useCalendar.ts
import { useState, useEffect } from "react";
import * as api from "../api/calendarApi";

export const useCalendar = () => {
  const [calendars, setCalendars] = useState([]);
  const [loading, setLoading] = useState(true);

  // 캘린더 목록 가져오기
  const fetchCalendars = async () => {
    setLoading(true);
    try {
      const data = await api.getAllCalendars();
      setCalendars(data);
    } catch (error) {
      console.error("캘린더를 불러오는 데 실패했습니다:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCalendars();
  }, []);

  // 캘린더 추가
  const createCalendar = async (calendarData) => {
    try {
      const newCalendar = await api.createCalendar(calendarData);
      setCalendars((prev) => [...prev, newCalendar]);
    } catch (error) {
      console.error("캘린더 생성 실패:", error);
    }
  };

  // 캘린더 수정
  const updateCalendar = async (id, calendarData) => {
    try {
      const updatedCalendar = await api.updateCalendar(id, calendarData);
      setCalendars((prev) =>
        prev.map((calendar) => (calendar.id === id ? updatedCalendar : calendar))
      );
    } catch (error) {
      console.error("캘린더 수정 실패:", error);
    }
  };

  // 캘린더 삭제
  const deleteCalendar = async (id) => {
    try {
      await api.deleteCalendar(id);
      setCalendars((prev) => prev.filter((calendar) => calendar.id !== id));
    } catch (error) {
      console.error("캘린더 삭제 실패:", error);
    }
  };

  return { calendars, loading, createCalendar, updateCalendar, deleteCalendar, fetchCalendars };
};