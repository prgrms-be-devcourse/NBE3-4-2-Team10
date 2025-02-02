import { useState } from "react";
import { getCoordinates } from "@/services/naverMapService";

const SearchLocation = ({ onLocationSelect }: { onLocationSelect: (lat: number, lng: number, address: string) => void }) => {
    const [address, setAddress] = useState("");

    const handleSearch = async () => {
        if (!address) return;

        console.log("🔍 입력한 주소:", address); // 주소 입력 확인

        const result = await getCoordinates(address);

        if (result) {
            const lat = parseFloat(result.y);
            const lng = parseFloat(result.x);
            console.log("✅ 검색된 좌표:", { lat, lng, roadAddress: result.roadAddress }); // ✅ 좌표 확인
            onLocationSelect(lat, lng, result.roadAddress);
        } else {
            alert("주소를 찾을 수 없습니다."); // 검색 실패 처리
        }
    };


    return (
        <div className="p-4">
            <input
                type="text"
                value={address}
                onChange={(e) => setAddress(e.target.value)}
                placeholder="주소 입력"
                className="border p-2 rounded w-full"
            />
            <button onClick={handleSearch} className="mt-2 p-2 bg-blue-500 text-white rounded">
                검색
            </button>
        </div>
    );
};

export default SearchLocation;
