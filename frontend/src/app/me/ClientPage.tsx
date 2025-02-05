"use client";

import { components } from "@/lib/backend/schema";

export default function ClientPage({
  me,
}: Readonly<{
  me: components["schemas"]["UserDto"];
}>) {
  return (
    <div className="flex flex-col gap-2">
      <div>nickname : {me.nickname}</div>
      <div className="text-gray-400">( 이메일, 가입일? 필요하면?? )</div>

      <div>nickname 변경 버튼</div>
    </div>
  );
}
