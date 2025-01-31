// src/services/naverMapService.ts

import axios from "axios";
import { Location } from "@/types/location";

const CLIENT_ID = process.env.NAVER_CLIENT_ID;
const CLIENT_SECRET = process.env.NAVER_CLIENT_SECRET;


// Geocoding (주소 → 좌표 변환)
interface NaverGeocodeResponse {
    addresses: {
        x: string; // 경도 (longitude)
        y: string; // 위도 (latitude)
        roadAddress?: string;
        jibunAddress?: string;
    }[];
}

// 적용된 코드:
export const getCoordinates = async (address: string): Promise<Location[]> => {
    try {
        const response = await axios.get<NaverGeocodeResponse>(
            "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode",
            {
                params: { query: address },
                headers: {
                    "X-NCP-APIGW-API-KEY-ID": CLIENT_ID,
                    "X-NCP-APIGW-API-KEY": CLIENT_SECRET,
                },
            }
        );

        if (response.data.addresses.length > 0) {
            return response.data.addresses.map((item) => ({
                latitude: parseFloat(item.y),
                longitude: parseFloat(item.x),
                address: item.roadAddress || item.jibunAddress || "", // 기본값 설정
            }));
        }

        return [];
    } catch (error) {
        console.error("Geocode API error:", error);
        return [];
    }
};



// Reverse Geocoding (좌표 → 주소 변환)
export const getAddress = async (lat: number, lng: number): Promise<Location[]> => {
    try {
        const response = await axios.get("https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc", {
            params: {
                coords: `${lng},${lat}`,
                orders: "roadaddr",
                output: "json",
            },
            headers: {
                "X-NCP-APIGW-API-KEY-ID": CLIENT_ID,
                "X-NCP-APIGW-API-KEY": CLIENT_SECRET,
            },
        });

        if (response.data.results.length && response.data.results.length> 0) {
            const result = response.data.results[0];
            return [
                {
                    latitude: lat,
                    longitude: lng,
                    address: `${result.region.area1.name} ${result.region.area2.name} ${result.region.area3.name}`,
                },
            ];
        }

        return [];
    } catch (error) {
        console.error("Reverse Geocode API error:", error);
        return [];
    }
};
