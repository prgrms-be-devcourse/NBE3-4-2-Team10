"use client";

import { useEffect, useState, useRef } from "react";
import { getCoordinates, getAddress } from "@/services/naverMapService";
import { Location } from "@/types/location";

// 네이버 맵 네임스페이스 타입 정의
declare global {
    interface Window {
        naver: typeof naver;
    }
}

export default function NaverMap() {
    const [address, setAddress] = useState("서울특별시 강남구 역삼동");
    const [title, setTitle] = useState("일정 제목");
    const [coordinates, setCoordinates] = useState<Location>({
        latitude: 37.5665,
        longitude: 126.9784,
        address: "",
    });
    const mapRef = useRef<HTMLDivElement | null>(null);
    const [map, setMap] = useState<naver.maps.Map | null>(null);
    const [marker, setMarker] = useState<naver.maps.Marker | null>(null);

    // 지도 초기화
    useEffect(() => {
        if (typeof window !== "undefined" && window.naver && mapRef.current) {
            const nmap = new window.naver.maps.Map(mapRef.current, {
                center: new window.naver.maps.LatLng(coordinates.latitude, coordinates.longitude),
                zoomControl: true,
                zoom: 13,
            });

            setMap(nmap);

            // 지도 클릭 이벤트 추가
            window.naver.maps.Event.addListener(nmap, "click", handleMapClick);
        }
    }, []);

    // 주소 → 좌표 변환 및 마커 업데이트
    const handleGeocode = async () => {
        const result = await getCoordinates(address);
        if (result.length > 0) {
            const location = result[0];
            setCoordinates(location);

            if (map) {
                map.setCenter(new window.naver.maps.LatLng(location.latitude, location.longitude));
                updateMarker(location);
            }
        } else {
            alert("주소를 찾을 수 없습니다.");
        }
    };

    // 좌표 → 주소 변환 및 마커 업데이트
    const handleReverseGeocode = async () => {
        const result = await getAddress(coordinates.latitude, coordinates.longitude);
        if (result.length > 0) {
            const location = result[0];
            setCoordinates(location);
            updateMarker(location);
        } else {
            alert("주소를 찾을 수 없습니다.");
        }
    };

    // 지도 클릭 이벤트 핸들러 (역지오코딩)
    const handleMapClick = async (event: naver.maps.PointerEvent) => {
        const coord = event.coord;
        const result = await getAddress(coord.lat(), coord.lng());

        if (result.length > 0) {
            const location = {
                latitude: coord.lat(),
                longitude: coord.lng(),
                address: result[0].address,
            };

            setCoordinates(location);
            updateMarker(location);

            if (map) {
                new window.naver.maps.InfoWindow({
                    content: `<div style='padding:10px;'>${result[0].address}</div>`,
                }).open(map, new window.naver.maps.LatLng(coord.lat(), coord.lng()));
            }
        }
    };

    // 마커 업데이트 함수
    const updateMarker = (location: Location) => {
        if (marker) marker.setMap(null); // 기존 마커 제거

        if (map) {
            const newMarker = new window.naver.maps.Marker({
                position: new window.naver.maps.LatLng(location.latitude, location.longitude),
                map: map,
            });

            setMarker(newMarker);
        }
    };


    return (
        <div>
            <h2>네이버 지도 API</h2>

            {/* 일정 제목 입력 */}
            <div>
                <input
                    type="text"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    placeholder="일정 제목을 입력하세요."
                />
            </div>

            {/* 주소 입력 및 변환 */}
            <div>
                <input
                    type="text"
                    value={address}
                    onChange={(e) => setAddress(e.target.value)}
                    placeholder="주소를 입력하세요."
                />
                <button onClick={handleGeocode}>주소 → 좌표 변환</button>
            </div>

            {/* 좌표 입력 및 변환 */}
            <div>
                <label>위도:</label>
                <input
                    type="number"
                    value={coordinates.latitude}
                    onChange={(e) => setCoordinates({...coordinates, latitude: parseFloat(e.target.value)})}
                    placeholder="위도 입력"
                />
                <label>경도:</label>
                <input
                    type="number"
                    value={coordinates.longitude}
                    onChange={(e) => setCoordinates({...coordinates, longitude: parseFloat(e.target.value)})}
                    placeholder="경도 입력"
                />
                <button onClick={handleReverseGeocode}>좌표 → 주소 변환</button>
            </div>

            {/* 지도 표시 */}
            <div ref={mapRef} style={{width: "600px", height: "400px"}}></div>

            {/* 현재 위치 정보 */}
            <p>현재 위치: {coordinates.address}</p>
        </div>
    );
}
