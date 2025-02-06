// 로그인 페이지 (브라우저)

"use client";


import { Button } from "@/components/ui/button";
import { faComments, faGlobe, faLock } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { MessageCircle } from "lucide-react";
import Link from "next/link";

export default function ClientPage() {
  const socialLoginForKakaoUrl = `http://localhost:8080/oauth2/authorization/kakao`;
  const socialLoginForGoogleUrl = `http://localhost:8080/oauth2/authorization/google`;
  const redirectUrlAfterSocialLogin = "http://localhost:3000";

  return (
    <>
      <div className="p-2">소셜 로그인</div>

      <div className="flex flex-col gap-2 mb-5">
        <Button variant="outline" asChild className="p-6 hover:bg-gray-200">
          <a
            href={`${socialLoginForKakaoUrl}?redirectUrl=${redirectUrlAfterSocialLogin}`}
          >
            <span className="flex gap-2 items-center">
              <FontAwesomeIcon icon={faComments} />
              카카오 로그인
            </span>
          </a>
        </Button>

        <Button variant="outline" asChild className="p-6 hover:bg-gray-200">
          <a
            href={`${socialLoginForGoogleUrl}?redirectUrl=${redirectUrlAfterSocialLogin}`}
          >
            <span className="flex gap-2 items-center">
              <FontAwesomeIcon icon={faGlobe} />
              구글 로그인
            </span>
          </a>
        </Button>
      </div>

      <hr />

      <div className="mt-5">
        <Button variant="outline" asChild className="p-6 hover:bg-gray-200">
          <Link href="/login/adminLogin">
            <FontAwesomeIcon icon={faLock} />
            관리자 로그인
          </Link>
        </Button>
      </div>
    </>
  );
}
