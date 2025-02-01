// 레이아웃 (브라우저)

"use client";

import Link from "next/link";
import React from "react";

export default function ClientLayput({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <>
      <header className="p-5">
        <div className="flex gap-5">
          <Link href="/">홈</Link>
          <Link href="/login">로그인</Link>
        </div>
      </header>

      <hr />

      <main className="flex-grow p-5">{children}</main>

      <hr />

      <footer className="p-5">Copyright 2025.</footer>
    </>
  );
}
