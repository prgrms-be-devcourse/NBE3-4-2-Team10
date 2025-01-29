// components/calendar/calendar/CalendarLayout/CalendarLayout.tsx
"use client";
import React, { useState } from "react";
// 추가된 import
import { Panel, PanelGroup, PanelResizeHandle } from "react-resizable-panels";
import { CalendarSidebar } from "../CalendarSidebar";
import { CalendarView } from "../CalendarView";
import { useCalendar } from "../../../../lib/calendar/hooks/useCalendar";
import type { Calendar } from "../../../../lib/calendar/types/calendarTypes";

export const CalendarLayout = () => {
  const { calendars, loading, createCalendar, updateCalendar, deleteCalendar } =
    useCalendar();

  const [selectedCalendar, setSelectedCalendar] = useState<Calendar | null>(
    null
  );

  const handleCreateCalendar = async () => {
    try {
      const name = prompt("캘린더 이름을 입력하세요");
      const description = prompt("캘린더 설명을 입력하세요");

      if (!name) return;

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

    try {
      const name = prompt(
        "새로운 캘린더 이름을 입력하세요",
        selectedCalendar.name
      );
      const description = prompt(
        "새로운 캘린더 설명을 입력하세요",
        selectedCalendar.description
      );

      if (!name) return;

      await updateCalendar(selectedCalendar.id, {
        name,
        description: description || "",
      });
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

    try {
      if (!confirm("정말 이 캘린더를 삭제하시겠습니까?")) return;

      await deleteCalendar(selectedCalendar.id);
      setSelectedCalendar(null);
      alert("캘린더가 삭제되었습니다.");
    } catch (err) {
      alert("캘린더 삭제에 실패했습니다.");
    }
  };

  const handleViewCalendar = () => {
    if (calendars.length === 0) {
      alert("조회할 캘린더가 없습니다.");
      return;
    }
  };

  return (
    <PanelGroup direction="horizontal" className="min-h-screen">
      <Panel
        defaultSize={20}
        minSize={15}
        maxSize={40}
        className="overflow-y-auto"
      >
        <CalendarSidebar
          onCreateClick={handleCreateCalendar}
          onUpdateClick={handleUpdateCalendar}
          onDeleteClick={handleDeleteCalendar}
          onViewClick={handleViewCalendar}
          selectedCalendar={selectedCalendar}
        />
      </Panel>

      <PanelResizeHandle className="w-2 bg-gray-200 hover:bg-gray-300 transition-colors">
        <div className="w-1 h-full mx-auto bg-gray-300" />
      </PanelResizeHandle>

      <Panel className="w-full">
        <CalendarView
          calendars={calendars}
          selectedCalendar={selectedCalendar}
          onCalendarSelect={setSelectedCalendar}
        />
      </Panel>
    </PanelGroup>
  );
};
