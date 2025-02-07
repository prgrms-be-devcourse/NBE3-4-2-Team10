// src/app/layout.tsx
import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import localFont from "next/font/local";
import "./globals.css";
import ClientLayout from "./ClientLayout";
import client from "@/lib/backend/client";
import { cookies } from "next/headers";
import { parseAccessToken } from "@/lib/auth/token";
import { config } from "@fortawesome/fontawesome-svg-core";
import "@fortawesome/fontawesome-svg-core/styles.css";
config.autoAddCss = false;
import NaverMapLoader from "@/components/schedule/NaverMapLoader"; // ✅ 추가


// ✅ 폰트 설정 유지
const geistSans = Geist({
    variable: "--font-geist-sans",
    subsets: ["latin"],
});

const geistMono = Geist({
    variable: "--font-geist-mono",
    subsets: ["latin"],
});

const pretendard = localFont({
    src: [
        {
            path: "../../node_modules/pretendard/dist/web/static/woff2/Pretendard-Black.woff2",
            weight: "45 920",
            style: "normal",
        },
    ],
    variable: "--font-pretendard",
});

// ✅ 기존 메타데이터 유지하며, 제목 변경
export const metadata: Metadata = {
    title: "Naver Map Schedule App",
    description: "Naver Map 연동 일정 관리 앱",
};

export default async function RootLayout({
                                             children,
                                         }: {
    children: React.ReactNode;
}) {
    const cookieStore = await cookies();
    const accessToken = cookieStore.get("accessToken")?.value;

    const { me, isLogin, isAdmin } = parseAccessToken(accessToken);

    return (
        <html lang="en">
        <head>
            <meta charSet="utf-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1" />
        </head>
        <body
            className={`${geistSans.variable} ${geistMono.variable} antialiased`}
        >
        <div
            className={`${pretendard.className} antialiased flex flex-col min-h-[100dvh]`}
        >
            {/* ✅ 네이버 지도 로더 추가 */}
            <NaverMapLoader>
                <ClientLayout me={me} isLogin={isLogin} isAdmin={isAdmin}>
                    {children}
                </ClientLayout>
            </NaverMapLoader>
        </div>
        </body>
        </html>
    );
}
