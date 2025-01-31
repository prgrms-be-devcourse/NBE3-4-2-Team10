import Script from "next/script";

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
