"use client";

import { useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import client from "@/lib/backend/client";

export default function ChangePasswordPage() {
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const router = useRouter();
  const searchParams = useSearchParams();
  const token = searchParams.get("token");

  const handlePasswordChange = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (password.length === 0 || confirmPassword.length === 0) {
      alert("비밀번호와 비밀번호 확인을 입력해주세요.");
      return;
    }

    if (password !== confirmPassword) {
      alert("비밀번호가 서로 일치하지 않습니다.");
      return;
    }

  };

  return (
    <>
      <h1 className="text-2xl font-bold">비밀번호 변경</h1>
      <form onSubmit={handlePasswordChange}>
        <div>
          <label>새 비밀번호</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="p-2"
            placeholder="새 비밀번호"
          />
        </div>
        <div>
          <label>비밀번호 확인</label>
          <input
            type="password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            className="p-2"
            placeholder="비밀번호 확인"
          />
        </div>
        <div>
          <input type="submit" value="비밀번호 변경" />
        </div>
      </form>
    </>
  );
}
