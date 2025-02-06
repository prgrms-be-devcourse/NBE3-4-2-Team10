"use client";
import React, { useState, useEffect } from "react";
import { Panel, PanelGroup, PanelResizeHandle } from "react-resizable-panels";
import { CalendarSidebar } from "../CalendarSidebar";
import { RightSidebar } from "../CalendarRightSidebar";
import { CalendarView } from "../CalendarView";
import { useCalendar } from "../../../../lib/calendar/hooks/useCalendar";
import type { Calendar } from "../../../../lib/calendar/types/calendarTypes";

export const CalendarLayout = () => {
  const { calendars, loading, createCalendar, updateCalendar, deleteCalendar } = useCalendar();
  const [selectedCalendar, setSelectedCalendar] = useState<Calendar | null>(null);

  const handleCreateCalendar = async () => {
    const name = prompt("캘린더 이름을 입력하세요");
    const description = prompt("캘린더 설명을 입력하세요");

    if (!name) return;

    try {
      await createCalendar({ name, description: description || "" });
      alert("캘린더가 생성되었습니다.");
    } catch (err) {
      alert("캘린더 생성에 실패했습니다.");
    }
  };

  const handleUpdateCalendar = async () => {
    if (!selectedCalendar) {
      alert("수정할 캘린더를 선택해주세요.");
      return;
    }

    const name = prompt("새로운 캘린더 이름을 입력하세요", selectedCalendar.name);
    const description = prompt("새로운 캘린더 설명을 입력하세요", selectedCalendar.description);

    if (!name) return;

    try {
      await updateCalendar(selectedCalendar.id, { name, description: description || "" });
      alert("캘린더가 수정되었습니다.");
    } catch (err) {
      alert("캘린더 수정에 실패했습니다.");
    }
  };

  const handleDeleteCalendar = async () => {
    if (!selectedCalendar) {
      alert("삭제할 캘린더를 선택해주세요.");
      return;
    }

    if (!confirm("정말 이 캘린더를 삭제하시겠습니까?")) return;

    try {
      await deleteCalendar(selectedCalendar.id);
      setSelectedCalendar(null);
      alert("캘린더가 삭제되었습니다.");
    } catch (err) {
      alert("캘린더 삭제에 실패했습니다.");
    }
  };

  return (
    <PanelGroup direction="horizontal" className="min-h-screen">
      <Panel defaultSize={15} minSize={10} maxSize={30}>
        <CalendarSidebar
          onCreateClick={handleCreateCalendar}
          onUpdateClick={handleUpdateCalendar}
          onDeleteClick={handleDeleteCalendar}
          selectedCalendar={selectedCalendar}
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
