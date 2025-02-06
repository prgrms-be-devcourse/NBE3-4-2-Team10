//src/lib/calendar/hooks/useCalendar.ts
import { useState, useEffect } from 'react';
import { calendarApi } from '../api/calendarApi';
import type { Calendar, CalendarCreateDto, CalendarUpdateDto } from '../types/calendarTypes';

export const useCalendar = () => {
  const [calendars, setCalendars] = useState<Calendar[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchCalendars = async () => {
    try {
      setLoading(true);
      const { data } = await calendarApi.getAllCalendars();
      setCalendars(data);
      setError(null);
    } catch (err) {
      setError(err as Error);
      console.error('Failed to fetch calendars:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCalendars();
  }, []);

  const createCalendar = async (calendarData: CalendarCreateDto) => {
    try {
      const { data: newCalendar } = await calendarApi.createCalendar(calendarData);
      setCalendars(prev => [...prev, newCalendar]);
      return newCalendar;
    } catch (err) {
      console.error('Failed to create calendar:', err);
      throw err;
    }
  };

  const updateCalendar = async (id: number, calendarData: CalendarUpdateDto) => {
    try {
      const { data: updatedCalendar } = await calendarApi.updateCalendar(id, calendarData);
      setCalendars(prev =>
          prev.map(calendar => (calendar.id === id ? updatedCalendar : calendar))
      );
      return updatedCalendar;
    } catch (err) {
      console.error('Failed to update calendar:', err);
      throw err;
    }
  };

  const deleteCalendar = async (id: number) => {
    try {
      await calendarApi.deleteCalendar(id);
      setCalendars(prev => prev.filter(calendar => calendar.id !== id));
    } catch (err) {
      console.error('Failed to delete calendar:', err);
      throw err;
    }
  };

  return {
    calendars,
    loading,
    error,
    createCalendar,
    updateCalendar,
    deleteCalendar,
    fetchCalendars,
  };
};
