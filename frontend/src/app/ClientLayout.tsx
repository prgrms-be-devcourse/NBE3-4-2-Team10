"use client";

import client from "@/lib/backend/client";
import { components } from "@/lib/backend/schema";
import Link from "next/link";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faHouse, faCopyright, faList, faPowerOff } from "@fortawesome/free-solid-svg-icons";

export default function ClientLayout({
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
  const [isHydrated, setIsHydrated] = useState(false);
  const router = useRouter();

  useEffect(() => {
    setIsHydrated(true);

    // ✅ 로그인한 사용자는 /calendar로 자동 이동
    if (isLogin) {
      router.push("/calendar");
    }
  }, [isLogin, router]);

  if (!isHydrated) {
    return null; // 클라이언트에서만 렌더링되도록 함
  }

  const logout = async () => {
    const response = await client.DELETE("/admin/logout");

    if (response.error) {
      alert(response.error.msg);
      return;
    }

    router.push("/login"); // ✅ 로그아웃 후 /login 페이지로 이동
  };

  return (
    <>
      <header className="p-5">
        <div className="flex gap-8">
          <Link href="/">
            <FontAwesomeIcon icon={faHouse} className="px-2" />홈
          </Link>
          {isLogin && <div>환영합니다. {me.username}님!</div>}
          {!isLogin && (
            <Link href="/login">
              <FontAwesomeIcon icon={faPowerOff} className="px-2" />
              로그인
            </Link>
          )}
          {isLogin && (
            <button onClick={logout}>
              <FontAwesomeIcon icon={faPowerOff} className="px-2" />
              로그아웃
            </button>
          )}
          {isAdmin && (
            <Link href="/admin/list">
              <FontAwesomeIcon icon={faList} className="px-2" />
              관리자
            </Link>
          )}
        </div>
      </header>

      <hr />

      <main className="flex-grow p-5">{children}</main>

      <hr />

      <footer className="p-5">
        <FontAwesomeIcon icon={faCopyright} className="px-2" />
        Copyright 2025.
      </footer>
    </>
  );
}