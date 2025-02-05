import axios from "axios";
import { NaverGeocodeResult } from "@/types/naverMapTypes";

/**
 * Reverse Geocoding (좌표 → 도로명 주소 변환)
 */
export const getAddress = async (lat: number, lng: number) => {
    try {
        const response = await axios.get(`/api/naverGeocode`, {
            params: { lat, lng },
        });

        const results: NaverGeocodeResult[] = response.data.results;

        if (!results || results.length === 0) {
            return "주소 없음";
        }

        let bestAddress = "";
        let roadAddress = "";
        let regionAddress = "";

        results.forEach((result: NaverGeocodeResult) => {
            const area1 = result.region.area1.name || "";
            const area2 = result.region.area2.name || "";
            const area3 = result.region.area3.name || "";
            const area4 = result.region.area4.name || "";

            const land = result.land;
            const roadName = land?.name || "";
            const roadNumber = land?.number1 ? `${land.number1}${land.number2 ? `-${land.number2}` : ""}` : "";
            const buildingName = land?.addition0?.value || ""; // 건물명

            // 건물명이 있으면 최우선 선택
            if (buildingName) {
                bestAddress = `${area1} ${area2} ${area3}, ${roadName} ${roadNumber} (${buildingName})`;
            }

            // 도로명 주소가 있으면 선택
            if (roadName && roadNumber) {
                roadAddress = `${area1} ${area2} ${area3}, ${roadName} ${roadNumber}`;
            }

            // 도로명 주소도 없으면 지역 주소 선택
            if (!regionAddress) {
                regionAddress = `${area1} ${area2} ${area3} ${area4}`.trim();
            }
        });

        // 건물명 포함된 주소가 있으면 최우선 반환
        if (bestAddress) return bestAddress;
        // 건물명이 없으면 도로명 주소 반환
        if (roadAddress) return roadAddress;
        // 도로명도 없으면 법정동 주소 반환
        return regionAddress || "주소 없음";
    } catch (error) {
        console.error("📛 Reverse Geocoding 요청 실패:", error);
        return "주소 가져오기 실패";
    }
};


/**
 * Geocoding (주소 → 좌표 변환)
 */
export const getCoordinates = async (address: string) => {
    try {
        const response = await axios.get(`/api/naverGeocode`, {
            params: { address },
        });
        return response.data.addresses[0] || null;
    } catch (error) {
        console.error("📛 Geocoding 요청 실패:", error);
        return null;
    }
};
