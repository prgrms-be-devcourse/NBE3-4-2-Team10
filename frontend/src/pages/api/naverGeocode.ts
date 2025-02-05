import axios from "axios";
import { NextApiRequest, NextApiResponse } from "next";

const NAVER_CLIENT_ID = process.env.NEXT_PUBLIC_NAVER_MAP_CLIENT_ID!;
const NAVER_CLIENT_SECRET = process.env.NAVER_CLIENT_SECRET!;

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    if (req.method !== "GET") {
        return res.status(405).json({ error: "Method not allowed" });
    }

    const { lat, lng, address } = req.query;

    try {
        let response;
        if (lat && lng) {
            // Reverse Geocoding (좌표 -> 주소 변환)
            response = await axios.get(
                `https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc`,
                {
                    params: { coords: `${lng},${lat}`, output: "json" },
                    headers: {
                        "X-NCP-APIGW-API-KEY-ID": NAVER_CLIENT_ID,
                        "X-NCP-APIGW-API-KEY": NAVER_CLIENT_SECRET,
                    },
                }
            );
        } else if (address) {
            // Geocoding (주소 -> 좌표 변환)
            response = await axios.get(
                `https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode`,
                {
                    params: { query: address },
                    headers: {
                        "X-NCP-APIGW-API-KEY-ID": NAVER_CLIENT_ID,
                        "X-NCP-APIGW-API-KEY": NAVER_CLIENT_SECRET,
                    },
                }
            );
        } else {
            return res.status(400).json({ error: "Invalid request parameters" });
        }

        res.status(200).json(response.data);
    } catch (error) {
        console.error("📛 네이버 API 요청 오류:", error);
        res.status(500).json({ error: "Failed to fetch data from Naver API" });
    }
}
