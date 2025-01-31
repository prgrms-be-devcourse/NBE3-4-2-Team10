import type { NextApiRequest, NextApiResponse } from "next";

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    const { lat, lon } = req.query;

    // 환경 변수로 API 키 관리 (직접 코드에 포함하면 보안에 취약)
    const CLIENT_ID = "up52slv6f0";
    const CLIENT_SECRET = "c4fb3xMjDIunzFbcdsOtJislvOZwnpcXhdZA5NHV";

    if (!lat || !lon) {
        return res.status(400).json({ error: "lat과 lon을 제공해야 합니다." });
    }

    const response = await fetch(
        `https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?center=${lon},${lat}&level=13&w=600&h=400`,
        {
            headers: {
                "X-NCP-APIGW-API-KEY-ID": CLIENT_ID,
                "X-NCP-APIGW-API-KEY": CLIENT_SECRET,
            },
        }
    );

    if (!response.ok) {
        return res.status(response.status).json({ error: "네이버 API 호출 실패" });
    }

    const imageBuffer = await response.arrayBuffer();
    res.setHeader("Content-Type", "image/png");
    res.send(Buffer.from(imageBuffer));
}
