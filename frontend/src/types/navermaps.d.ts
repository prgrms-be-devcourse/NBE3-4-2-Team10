declare namespace naver {
    namespace maps {
        interface MapOptions {
            center: LatLng;
            zoom?: number;
            zoomControl?: boolean;
            size?: Size;
        }

        class Map {
            constructor(element: HTMLElement, options: MapOptions);
            setCenter(latlng: LatLng): void;
            getCenter(): LatLng;
        }

        class LatLng {
            constructor(lat: number, lng: number);
            lat(): number;
            lng(): number;
        }

        class MarkerOptions {
            position: LatLng;
            map?: Map;
            title?: string;
        }

        class Marker {
            constructor(options: MarkerOptions);
        }

        class InfoWindow {
            constructor(options: { content: string });
            open(map: Map, position: LatLng): void;
            close(): void;
        }

        class Size {
            constructor(width: number, height: number);
        }

        class PointerEvent {
            coord: LatLng;
        }

        class Event {
            static addListener(instance: Map | Marker | InfoWindow, eventName: string, handler: (event: PointerEvent) => void): void;
        }
    }
}
