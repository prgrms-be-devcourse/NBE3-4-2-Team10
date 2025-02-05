export interface Region {
    area1: { name: string };
    area2: { name: string };
    area3: { name: string };
    area4: { name: string };
}

export interface Land {
    name?: string; // 도로명
    number1?: string; // 도로번호 앞자리
    number2?: string; // 도로번호 뒷자리 (없을 수도 있음)
    addition0?: { value: string }; // 건물명 (없을 수도 있음)
}

export interface NaverGeocodeResult {
    region: Region;
    land?: Land; // 일부 주소에는 land가 없을 수 있음
}
