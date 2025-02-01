// 레이아웃 (브라우저)

"use client";

import client from "@/lib/backend/client";
import { components } from "@/lib/backend/schema";
import Link from "next/link";
import { useRouter } from "next/navigation";
import React from "react";

export default function ClientLayput({
  children,
  me,
}: Readonly<{
  children: React.ReactNode;
  me: components["schemas"]["UserDto"];
}>) {
  const isLogin = me.id !== 0;
  const router = useRouter();

  const logout = async () => {
    const response = await client.DELETE("/admin/logout");

    if (response.error) {
      alert(response.error.msg);
      return;
    }

    window.location.replace("/");
  };

  return (
    <>
      <header className="p-5">
        <div className="flex gap-5">
          <Link href="/">홈</Link>
          {!isLogin && <Link href="/login">로그인</Link>}
          {isLogin && <button onClick={logout}>로그아웃</button>}
        </div>
      </header>

      <hr />

      <main className="flex-grow p-5">{children}</main>

      <hr />

      <footer className="p-5">Copyright 2025.</footer>
    </>
  );
}
