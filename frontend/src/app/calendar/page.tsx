// page.tsx
"use client";
import { CalendarLayout } from "@/components/calendar/calendar/CalendarLayout";
import { LoginRequired } from "./layout";

export default LoginRequired(CalendarLayout);
