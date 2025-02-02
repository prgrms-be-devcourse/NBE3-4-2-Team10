import { useEffect, useState, createContext, useContext } from "react";
import Head from "next/head";
import "../styles/globals.css";

// Context 생성
const NaverMapContext = createContext(false);

export const useNaverMapLoaded = () => useContext(NaverMapContext);

export default function MyApp({ Component, pageProps }: { Component: any; pageProps: any }) {
    const [isNaverMapLoaded, setIsNaverMapLoaded] = useState(false);

    useEffect(() => {
        if (typeof window !== "undefined" && !window.naver) {
            const script = document.createElement("script");
            script.src = `https://oapi.map.naver.com/openapi/v3/maps.js?ncpClientId=${process.env.NEXT_PUBLIC_NAVER_MAP_CLIENT_ID}`;
            script.async = true;
            script.onload = () => {
                console.log("✅ 네이버 지도 스크립트 로드 완료");
                setIsNaverMapLoaded(true); // 스크립트 로드 완료 시 상태 변경
            };
            document.head.appendChild(script);
        } else {
            setIsNaverMapLoaded(true); // 이미 로드된 경우
        }
    }, []);

    return (
        <NaverMapContext.Provider value={isNaverMapLoaded}>
            <Head>
                <title>네이버 지도 일정 관리</title>
            </Head>
            <Component {...pageProps} />
        </NaverMapContext.Provider>
    );
}
