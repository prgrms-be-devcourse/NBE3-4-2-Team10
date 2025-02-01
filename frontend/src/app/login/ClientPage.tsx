// 로그인 페이지 (브라우저)

"use client";

import Link from "next/link";

export default function ClientPage() {
  return (
    <>
      <div className="p-5">소셜 로그인 (이후 버튼으로 변경)</div>

      <hr />

      <div className="p-5">
        <Link href="/login/adminLogin">관리자 로그인</Link>
      </div>
    </>
  );
}
