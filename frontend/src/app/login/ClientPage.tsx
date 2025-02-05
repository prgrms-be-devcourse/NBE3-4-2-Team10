// 로그인 페이지 (브라우저)

"use client";

import { Button } from "@/components/schedule/ui/button";
import { MessageCircle } from "lucide-react";
import Link from "next/link";

export default function ClientPage() {
  const socialLoginForKakaoUrl = `http://localhost:8080/oauth2/authorization/kakao`;
  const redirectUrlAfterSocialLogin = "http://localhost:3000";

  return (
    <>
      <div className="p-2">소셜 로그인 (이후 버튼으로 변경)</div>

      <div className="">
        <Button variant="outline" asChild>
          <a
            href={`${socialLoginForKakaoUrl}?redirectUrl=${redirectUrlAfterSocialLogin}`}
          >
            <MessageCircle />
            <span className="font-bold">카카오 로그인</span>
          </a>
        </Button>
      </div>

      <hr />

      <div className="p-2">
        <Link href="/login/adminLogin">관리자 로그인</Link>
      </div>
    </>
  );
}
