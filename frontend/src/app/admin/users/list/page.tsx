// 회원 리스트 페이지 (서버)

import client from "@/lib/backend/client";
import ClientPage from "./ClientPage";
import { cookies } from "next/headers";

export default async function Page({
  searchParams,
}: {
  searchParams: {
    searchKeywordType?: string;
    searchKeyword?: string;
    pageSize?: number;
    page?: number;
  };
}) {
  const {
    searchKeyword = "",
    searchKeywordType = "username",
    pageSize = 10,
    page = 1,
  } = await searchParams;
  const response = await client.GET("/admin/users", {
    params: {
      query: {
        searchKeyword,
        searchKeywordType,
        pageSize,
        page,
      },
    },
    headers: {
      cookie: (await cookies()).toString(),
    },
  });
  const itemPage = response.data?.data;
  return (
    <>
      <ClientPage
        searchKeyword={searchKeyword}
        searchKeywordType={searchKeywordType}
        page={page}
        pageSize={pageSize}
        itemPage={itemPage!!}
      />
    </>
  );
}
