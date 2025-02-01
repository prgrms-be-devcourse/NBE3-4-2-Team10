// 관리자 로그인 페이지 (브라우저)

"use client";

export default function ClientPage() {
  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const form = e.target as HTMLFormElement;

    // 프론트 측 입력값 검사
    if (form.username.value.length === 0) {
      alert("아이디를 입력해주세요.");
      form.username.focus();

      return;
    }

    if (form.password.value.length === 0) {
      alert("비밀번호를 입력해주세요.");
      form.password.focus();

      return;
    }

    // 나중에 openapi-fetch로 변경
    const response = await fetch("/admin/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: form.username.value,
        password: form.password.value,
      }),
    });

    const data = await response.json();

    if (data.error) {
      alert(data.error.msg);
      return;
    }

    alert(data.msg);

    window.location.replace("/");
  };

  return (
    <>
      <h1 className="text-2xl font-bold">관리자 로그인</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label>아이디</label>
          <input
            type="text"
            name="username"
            className="p-2"
            placeholder="아이디"
          />
        </div>
        <div>
          <label>비밀번호</label>
          <input
            type="password"
            name="password"
            className="p-2"
            placeholder="비밀번호"
          />
        </div>
        <div>
          <input type="submit" value="로그인" />
        </div>
      </form>
    </>
  );
}
