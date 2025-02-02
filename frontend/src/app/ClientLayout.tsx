// 레이아웃 (브라우저)

"use client";

import client from "@/lib/backend/client";
import { components } from "@/lib/backend/schema";
import Link from "next/link";
import React from "react";

export default function ClientLayput({
  children,
  me,
  isLogin,
  isAdmin,
}: Readonly<{
  children: React.ReactNode;
  me: components["schemas"]["UserDto"];
  isLogin: boolean;
  isAdmin: boolean;
}>) {
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
          {isAdmin && <Link href="/admin/list">관리자</Link>}
        </div>
      </header>

      <hr />

      <main className="flex-grow p-5">{children}</main>

      <hr />

      <footer className="p-5">Copyright 2025.</footer>
    </>
  );
}
