import Script from "next/script";

export default function Layout({ children }: { children: React.ReactNode }) {
    return (
        <html lang="ko">
        <head>
            <Script
                strategy="beforeInteractive"
                src={`https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=${process.env.NEXT_PUBLIC_MAP_KEY}&submodules=geocoder`}
            />
        </head>
        <body>{children}</body>
        </html>
    );
}
