import React from "react";

interface CalendarLayoutProps {
  children: React.ReactNode;
}

export default function CalendarRootLayout({ children }: CalendarLayoutProps) {
  return <div className="min-h-screen bg-gray-100">{children}</div>;
}
