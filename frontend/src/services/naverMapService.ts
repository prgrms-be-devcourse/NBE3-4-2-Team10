import axios from "axios";

const NAVER_CLIENT_ID = process.env.NEXT_PUBLIC_NAVER_MAP_CLIENT_ID;
const NAVER_CLIENT_SECRET = process.env.NEXT_PUBLIC_NAVER_MAP_CLIENT_SECRET;

/**
 * 📍 Reverse Geocoding (좌표 → 도로명 주소 변환)
 */
export const getAddress = async (lat: number, lng: number) => {
    const response = await axios.get(
        `https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc`,
        {
            params: {
                coords: `${lng},${lat}`,
                output: "json",
            },
            headers: {
                "X-NCP-APIGW-API-KEY-ID": NAVER_CLIENT_ID,
                "X-NCP-APIGW-API-KEY": NAVER_CLIENT_SECRET,
            },
        }
    );
    return response.data.results[0]?.region?.area1?.name || "주소 없음";
};

/**
 * Geocoding (주소 → 좌표 변환)
 */
export const getCoordinates = async (address: string) => {
    const response = await axios.get(
        `https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode`,
        {
            params: { query: address },
            headers: {
                "X-NCP-APIGW-API-KEY-ID": NAVER_CLIENT_ID,
                "X-NCP-APIGW-API-KEY": NAVER_CLIENT_SECRET,
            },
        }
    );
    return response.data.addresses[0] || null;
};
