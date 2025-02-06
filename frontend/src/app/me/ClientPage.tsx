"use client";

import { Button } from "@/components/ui/button";
import { components } from "@/lib/backend/schema";
import Link from "next/link";

export default function ClientPage({
  me,
}: Readonly<{
  me: components["schemas"]["UserDto"];
}>) {
  return (
    <div className="flex flex-col gap-2">
      <div>nickname : {me.nickname}</div>
      {/* ( 이메일, 가입일? 필요하면?? ) */}

      <div className="">
        <Button variant="outline" asChild className="hover:bg-gray-200">
          <Link href="/me/modify">내정보 수정</Link>
        </Button>
      </div>
    </div>
  );
}
