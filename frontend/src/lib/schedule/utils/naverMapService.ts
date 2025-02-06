import axios from "axios";
import { NaverGeocodeApiResponse } from "@/types/naverMapTypes";

/**
 * Reverse Geocoding (좌표 → 도로명 주소 변환)
 */
export const getAddress = async (lat: number, lng: number): Promise<string> => {
    try {
        const response = await axios.get<NaverGeocodeApiResponse>("/api/naverGeocode", {
            params: { lat, lng },
        });

        const results = response.data.results;

        if (!results || results.length === 0) {
            return "주소 없음";
        }

        let bestAddress = "";
        let roadAddress = "";
        let regionAddress = "";

        results.forEach((result) => {
            const area1 = result.region.area1.name || "";
            const area2 = result.region.area2.name || "";
            const area3 = result.region.area3.name || "";
            const area4 = result.region.area4.name || "";

            const land = result.land;
            const roadName = land?.name || "";
            const roadNumber = land?.number1 ? `${land.number1}${land.number2 ? `-${land.number2}` : ""}` : "";
            const buildingName = land?.addition0?.value || ""; // 건물명

            if (buildingName) {
                bestAddress = `${area1} ${area2} ${area3}, ${roadName} ${roadNumber} (${buildingName})`;
            }

            if (roadName && roadNumber) {
                roadAddress = `${area1} ${area2} ${area3}, ${roadName} ${roadNumber}`;
            }

            if (!regionAddress) {
                regionAddress = `${area1} ${area2} ${area3} ${area4}`.trim();
            }
        });

        if (bestAddress) return bestAddress;
        if (roadAddress) return roadAddress;
        return regionAddress || "주소 없음";
    } catch (error) {
        console.error("📛 Reverse Geocoding 요청 실패:", error);
        return "주소 가져오기 실패";
    }
};

/**
 * Geocoding (주소 → 좌표 변환)
 */
export const getCoordinates = async (address: string): Promise<{ x: string; y: string; roadAddress: string } | null> => {
    try {
        console.log("🔍 Geocoding 요청 시작 - 주소:", address);

        const response = await axios.get<NaverGeocodeApiResponse>("/api/naverGeocode", {
            params: { address },
        });

        console.log("🌍 네이버 Geocoding API 응답:", response.data);

        // ✅ 응답 구조 변경 확인
        if (!response.data || !response.data.addresses || response.data.addresses.length === 0) {
            console.error("📛 주소 검색 실패: API 응답이 비어 있거나 유효하지 않습니다.", response.data);
            return null;
        }

        const result = response.data.addresses[0]; // addresses 배열에서 첫 번째 결과 사용

        return {
            x: result.x, // ✅ 변경된 응답 구조 적용
            y: result.y,
            roadAddress: result.roadAddress || result.jibunAddress || "주소 없음",
        };
    } catch (error) {
        console.error("📛 Geocoding 요청 실패:", error);
        return null;
    }
};
