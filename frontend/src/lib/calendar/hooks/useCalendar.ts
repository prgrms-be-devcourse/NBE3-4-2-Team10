// lib/calendar/hooks/useCalendar.ts
import { useState, useEffect } from "react";
import { calendarApi } from "../api/calendarApi";
import type {
  Calendar,
  CalendarCreateDto,
  CalendarUpdateDto,
} from "../types/calendarTypes";

export const useCalendar = () => {
  const [calendars, setCalendars] = useState<Calendar[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchCalendars = async () => {
    try {
      setLoading(true);
      const response = await calendarApi.getAll();
      setCalendars(response.data);
    } catch (err: any) {
      setError(err);
    } finally {
      setLoading(false);
    }
  };

  const createCalendar = async (dto: CalendarCreateDto) => {
    try {
      const response = await calendarApi.create(dto);
      setCalendars((prev) => [...prev, response.data]);
      return response.data;
    } catch (err) {
      throw err;
    }
  };

  const updateCalendar = async (id: number, dto: CalendarUpdateDto) => {
    try {
      const response = await calendarApi.update(id, dto);
      setCalendars((prev) =>
        prev.map((calendar) => (calendar.id === id ? response.data : calendar))
      );
      return response.data;
    } catch (err) {
      throw err;
    }
  };

  const deleteCalendar = async (id: number) => {
    try {
      await calendarApi.delete(id);
      setCalendars((prev) => prev.filter((calendar) => calendar.id !== id));
    } catch (err) {
      throw err;
    }
  };

  useEffect(() => {
    fetchCalendars();
  }, []);

  return {
    calendars,
    loading,
    error,
    createCalendar,
    updateCalendar,
    deleteCalendar,
    refreshCalendars: fetchCalendars,
  };
};
