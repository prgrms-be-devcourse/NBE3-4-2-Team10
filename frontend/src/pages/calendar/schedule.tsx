import { useState } from "react";
import DynamicMap from "@/components/DynamicMap";
import SearchLocation from "@/components/SearchLocation";

export default function Schedule() {
    const [location, setLocation] = useState<{ lat: number; lng: number; address: string } | null>({
        lat: 37.5665,
        lng: 126.9780,
        address: "서울 시청",
    });

    const handleLocationSelect = (lat: number, lng: number, address: string) => {
        console.log("📌 선택된 위치 업데이트:", { lat, lng, address }); // 디버깅 로그
        setLocation({ lat, lng, address });
    };

    return (
        <div className="p-4">
            <h1 className="text-xl font-bold">일정 추가</h1>
            <SearchLocation onLocationSelect={handleLocationSelect} />
            {location && (
                <>
                    <DynamicMap
                        latitude={location.lat}
                        longitude={location.lng}
                        onLocationSelect={handleLocationSelect}
                    />
                    <div className="mt-4 p-2 border rounded">
                        <p>선택한 장소: {location.address}</p>
                        <p>위도: {location.lat}</p>
                        <p>경도: {location.lng}</p>
                    </div>
                </>
            )}
        </div>
    );
}
