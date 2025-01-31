// src/services/naverMapService.ts

import axios from "axios";
import { Location } from "@/types/location";

const CLIENT_ID = "up52slv6f0";
const CLIENT_SECRET = "c4fb3xMjDIunzFbcdsOtJislvOZwnpcXhdZA5NHV";

// 📌 Geocoding (주소 → 좌표 변환)
export const getCoordinates = async (address: string): Promise<Location[]> => {
    const response = await axios.get("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode", {
        params: { query: address },
        headers: {
            "X-NCP-APIGW-API-KEY-ID": CLIENT_ID,
            "X-NCP-APIGW-API-KEY": CLIENT_SECRET,
        },
    });

    return response.data.addresses.map((item: any) => ({
        latitude: parseFloat(item.y),
        longitude: parseFloat(item.x),
        address: item.roadAddress || item.jibunAddress,
    }));
};

// 📌 Reverse Geocoding (좌표 → 주소 변환)
export const getAddress = async (lat: number, lng: number): Promise<Location[]> => {
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

    return response.data.results.map((result: any) => ({
        latitude: lat,
        longitude: lng,
        address: result.region.area1.name + " " + result.region.area2.name + " " + result.region.area3.name,
    }));
};
