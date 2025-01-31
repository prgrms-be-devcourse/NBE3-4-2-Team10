"use client";

import { useEffect, useState, useRef } from "react";
import { getCoordinates, getAddress } from "@/services/naverMapService";
import { Location } from "@/types/location";

// 네이버 맵 네임스페이스 선언
declare global {
    interface Window {
        naver: any;
    }
}

export default function NaverMap() {
    const [address, setAddress] = useState("서울특별시 강남구 역삼동");
    const [coordinates, setCoordinates] = useState<Location>({
        latitude: 37.5665,
        longitude: 126.9784,
        address: "",
    });
    const mapRef = useRef<HTMLDivElement | null>(null);
    let nmap: any;

    useEffect(() => {
        if (typeof window !== "undefined" && window.naver && mapRef.current) {
            nmap = new window.naver.maps.Map(mapRef.current, {
                center: new window.naver.maps.LatLng(coordinates.latitude, coordinates.longitude),
                zoomControl: true,
                zoom: 13,
                size: { width: 600, height: 400 },
            });

            window.naver.maps.Event.addListener(nmap, "click", function (e: any) {
                handleMapClick(e.coord);
            });
        }
    }, [mapRef]);

    const handleGeocode = async () => {
        const result = await getCoordinates(address);
        if (result.length > 0) {
            setCoordinates(result[0]);
            if (typeof window !== "undefined" && window.naver) {
                nmap.setCenter(new window.naver.maps.LatLng(result[0].latitude, result[0].longitude));
            }
        }
    };

    const handleReverseGeocode = async () => {
        const result = await getAddress(coordinates.latitude, coordinates.longitude);
        if (result.length > 0) {
            setCoordinates(result[0]);
        }
    };

    const handleMapClick = async (coord: any) => {
        const result = await getAddress(coord.y, coord.x);
        if (result.length > 0) {
            setCoordinates(result[0]);
            new window.naver.maps.InfoWindow({
                content: `<div style='padding:10px;'>${result[0].address}</div>`
            }).open(nmap, coord);
        }
    };

    return (
        <div>
            <h2>네이버 지도 API</h2>

            {/* Geocoding (주소 → 좌표 변환) */}
            <div>
                <input
                    type="text"
                    value={address}
                    onChange={(e) => setAddress(e.target.value)}
                    placeholder="주소를 입력하세요."
                />
                <button onClick={handleGeocode}>주소 → 좌표 변환</button>
            </div>

            {/* Reverse Geocoding (좌표 → 주소 변환) */}
            <div>
                <label>위도:</label>
                <input
                    type="number"
                    value={coordinates.latitude}
                    onChange={(e) => setCoordinates({ ...coordinates, latitude: parseFloat(e.target.value) })}
                    placeholder="위도 입력"
                />
                <label>경도:</label>
                <input
                    type="number"
                    value={coordinates.longitude}
                    onChange={(e) => setCoordinates({ ...coordinates, longitude: parseFloat(e.target.value) })}
                    placeholder="경도 입력"
                />
                <button onClick={handleReverseGeocode}>좌표 → 주소 변환</button>
            </div>

            {/* 지도 표시 */}
            <div ref={mapRef} style={{ width: "600px", height: "400px" }}></div>

            {/* Static Map 표시 */}
            <img
                src={`https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?center=${coordinates.longitude},${coordinates.latitude}&level=13&w=600&h=400&X-NCP-APIGW-API-KEY-ID=up52slv6f0`}
                alt="네이버 정적 지도"
            />

            <p>현재 위치: {coordinates.address}</p>
        </div>
    );
}
