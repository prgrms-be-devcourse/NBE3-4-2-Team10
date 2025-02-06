"use client";
import client from "@/lib/backend/client";
import {useRouter} from "next/navigation";
import Script from "next/script";
import {useEffect, useState} from "react";

//  로그아웃로직에 JSESSIONID 를추가로제거하는게필요함(세션에도인증정보가들어있음)
//  아니면로그인시에세션에인증정보를넣지말아야함.

export const LoginRequired = <T,>(WrappedComponent: React.FC<T>) => {
    const InnerFunction: React.FC<T> = (props: T) => {
        const router = useRouter();
        const [loaded, setLoaded] = useState(false);
        useEffect(() => {
            client
                .GET("/user/me")
                .then(({data}) => {
                    if (data?.id) {
                        console.log(data);
                        setLoaded(true);
                    } else {
                        console.log("no user");
                        router.push("/login");
                    }
                })
                .catch(() => {
                    router.push("/login");
                });
        }, []);
        if (!loaded) return <></>;
        return <WrappedComponent {...(props as any)} />;
    };
    return InnerFunction;
};

export default function CalendarLayout({
                                           children,
                                       }: {
    children: React.ReactNode;
}) {
    return (
        <div>
            <Script
                src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.15/index.global.min.js"
                strategy="afterInteractive"
            />
            <link
                href="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.15/main.min.css"
                rel="stylesheet"
            />
            {children}
        </div>
    );
}