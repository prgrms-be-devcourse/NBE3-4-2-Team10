// src/components/calendar/calendar/CalendarLayout/CalendarLayout.tsx
"use client";
import React, { useState, useEffect } from 'react';
import { Panel, PanelGroup, PanelResizeHandle } from 'react-resizable-panels';
import { CalendarSidebar } from '../CalendarSidebar';
import { RightSidebar } from '../CalendarRightSidebar';
import { CalendarView } from '../CalendarView';
import { useCalendar } from '@/lib/calendars/hooks/useCalendar';
import type { Calendar } from '@/lib/calendars/types/calendarTypes';

export const CalendarLayout = () => {
  const { calendars, loading, error, createCalendar, updateCalendar, deleteCalendar, fetchCalendars } = useCalendar();
  const [selectedCalendar, setSelectedCalendar] = useState<Calendar | null>(null);

  const handleCreateCalendar = async () => {
    const name = prompt('캘린더 이름을 입력하세요');
    if (!name) return;

    const description = prompt('캘린더 설명을 입력하세요') || '';

    try {
      const newCalendar = await createCalendar({ name, description });
      await fetchCalendars(); // 목록 새로고침
      setSelectedCalendar(newCalendar);
      alert('캘린더가 생성되었습니다.');
    } catch (error) {
      console.error('캘린더 생성 실패:', error);
      alert('캘린더 생성에 실패했습니다. 다시 시도해주세요.');
    }
  };

  const handleUpdateCalendar = async () => {
    if (!selectedCalendar) {
      alert('수정할 캘린더를 선택해주세요.');
      return;
    }

    const name = prompt('새로운 캘린더 이름을 입력하세요', selectedCalendar.name);
    if (!name) return;

    const description = prompt('새로운 캘린더 설명을 입력하세요', selectedCalendar.description);

    try {
      const updatedCalendar = await updateCalendar(selectedCalendar.id, {
        name,
        description: description || ''
      });
      await fetchCalendars(); // 목록 새로고침
      setSelectedCalendar(updatedCalendar);
      alert('캘린더가 수정되었습니다.');
    } catch (error) {
      console.error('캘린더 수정 실패:', error);
      alert('캘린더 수정에 실패했습니다. 다시 시도해주세요.');
    }
  };

  const handleDeleteCalendar = async () => {
    if (!selectedCalendar) {
      alert('삭제할 캘린더를 선택해주세요.');
      return;
    }

    if (!confirm('정말 이 캘린더를 삭제하시겠습니까?')) return;

    try {
      await deleteCalendar(selectedCalendar.id);
      await fetchCalendars(); // 목록 새로고침
      setSelectedCalendar(null);
      alert('캘린더가 삭제되었습니다.');
    } catch (error) {
      console.error('캘린더 삭제 실패:', error);
      alert('캘린더 삭제에 실패했습니다. 다시 시도해주세요.');
    }
  };

  const handleViewCalendar = () => {
    if (calendars.length === 0) {
      alert('조회할 캘린더가 없습니다.');
      return;
    }
    // 현재 선택된 캘린더가 없다면 첫 번째 캘린더 선택
    if (!selectedCalendar && calendars.length > 0) {
      setSelectedCalendar(calendars[0]);
    }
  };

  // 컴포넌트 마운트 시 캘린더 목록 새로고침
  useEffect(() => {
    fetchCalendars();
  }, []);

  if (loading) {
    return <div className="flex items-center justify-center min-h-screen">로딩 중...</div>;
  }

  if (error) {
    return <div className="flex items-center justify-center min-h-screen">에러가 발생했습니다. 페이지를 새로고침 해주세요.</div>;
  }

  return (
      <PanelGroup direction="horizontal" className="min-h-screen">
        <Panel defaultSize={15} minSize={10} maxSize={30}>
          <CalendarSidebar
              onCreateClick={handleCreateCalendar}
              onUpdateClick={handleUpdateCalendar}
              onDeleteClick={handleDeleteCalendar}
              onViewClick={handleViewCalendar}
              selectedCalendar={selectedCalendar}
              calendars={calendars}
              onCalendarSelect={setSelectedCalendar}
          />
        </Panel>

        <PanelResizeHandle className="w-1.5 hover:bg-blue-200 transition-colors">
          <div className="w-full h-full bg-gray-300" />
        </PanelResizeHandle>

        <Panel>
          <CalendarView
              calendars={calendars}
              selectedCalendar={selectedCalendar}
              onCalendarSelect={setSelectedCalendar}
          />
        </Panel>

        <PanelResizeHandle className="w-1.5 hover:bg-blue-200 transition-colors">
          <div className="w-full h-full bg-gray-300" />
        </PanelResizeHandle>

        <Panel defaultSize={15} minSize={10} maxSize={30}>
          <RightSidebar />
        </Panel>
      </PanelGroup>
  );
};