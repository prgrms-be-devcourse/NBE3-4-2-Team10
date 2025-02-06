"use client";

import client from "@/lib/backend/client";

export default function VerifyPage() {
  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const form = e.target as HTMLFormElement;

    if (form.username.value.length === 0) {
      alert("아이디를 입력해주세요.");
      form.username.focus();
      return;
    }

    if (form.verificationCode.value.length === 0) {
      alert("인증번호를 입력해주세요.");
      form.verificationCode.focus();
      return;
    }

    // 인증번호 검증 요청
    const response = await client.POST("/admin/verify", {
      body: {
        username: form.username.value,
        verificationCode: form.verificationCode.value,
      },
    });

    if (response.error) {
      alert(response.error.msg);
      return;
    }

    alert("인증에 성공했습니다. 로그인해주세요.");
    window.location.replace("/login/adminLogin");  // 인증 성공 후 관리자 로그인 페이지로 이동
  };

  return (
    <>
      <h1 className="text-2xl font-bold">인증번호 입력</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label>아이디</label>
          <input
            type="text"
            name="username"
            className="p-2"
            placeholder="아이디를 입력하세요"
          />
        </div>
        <div>
          <label>인증번호</label>
          <input
            type="text"
            name="verificationCode"
            className="p-2"
            placeholder="인증번호를 입력하세요"
          />
        </div>
        <div>
          <input type="submit" value="인증하기" />
        </div>
      </form>
    </>
  );
}