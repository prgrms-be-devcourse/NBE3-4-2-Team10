// lib/calendar/hooks/useCalendar.ts
import { useState, useEffect } from 'react';
import * as api from '../api/calendarApi';  // API 함수 가져오기

export const useCalendar = () => {
  const [calendars, setCalendars] = useState([]);
  const [loading, setLoading] = useState<boolean>(false);

  // 캘린더 목록을 가져오는 함수
  const fetchCalendars = async () => {
    setLoading(true);
    try {
      const data = await api.getAllCalendars();
      setCalendars(data);
    } catch (error) {
      console.error('Error fetching calendars:', error);
    } finally {
      setLoading(false);
    }
  };

  // 캘린더 생성 함수
  const createCalendar = async (calendarData) => {
    try {
      const newCalendar = await api.createCalendar(calendarData);
      setCalendars((prevCalendars) => [...prevCalendars, newCalendar]);
    } catch (error) {
      console.error('Error creating calendar:', error);
    }
  };

  // 캘린더 수정 함수
  const updateCalendar = async (id, calendarData) => {
    try {
      const updatedCalendar = await api.updateCalendar(id, calendarData);
      setCalendars((prevCalendars) =>
        prevCalendars.map((calendar) =>
          calendar.id === id ? updatedCalendar : calendar
        )
      );
    } catch (error) {
      console.error('Error updating calendar:', error);
    }
  };

  // 캘린더 삭제 함수
  const deleteCalendar = async (id) => {
    try {
      await api.deleteCalendar(id);
      setCalendars((prevCalendars) =>
        prevCalendars.filter((calendar) => calendar.id !== id)
      );
    } catch (error) {
      console.error('Error deleting calendar:', error);
    }
  };

  // 컴포넌트가 처음 렌더링될 때 캘린더 목록을 가져옵니다.
  useEffect(() => {
    fetchCalendars();
  }, []);

  return {
    calendars,
    loading,
    createCalendar,
    updateCalendar,
    deleteCalendar,
    fetchCalendars,  // 캘린더 목록을 다시 가져오는 함수도 제공
  };
};