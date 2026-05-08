'use client';

import React, { useEffect, useState } from 'react';
import { MapContainer, TileLayer, Marker, Popup, CircleMarker, LayerGroup, Tooltip } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';

// Custom icons for marker status
const onlineIcon = L.icon({
  iconUrl: '/icons/marker-online.png', // Placeholder - need to create these icons
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowUrl: '/icons/marker-shadow.png', // Placeholder
  shadowSize: [41, 41],
});

const offlineIcon = L.icon({
  iconUrl: '/icons/marker-offline.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowUrl: '/icons/marker-shadow.png',
  shadowSize: [41, 41],
});

interface CameraLocation {
  id: string;
  name: string;
  latitude: number;
  longitude: number;
  status: 'ONLINE' | 'OFFLINE';
}

interface InteractiveMapProps {
  cameras: CameraLocation[];
}

export function InteractiveMap({ cameras }: InteractiveMapProps) {
  const [mapCenter, setMapCenter] = useState<[number, number]>([-23.5505, -46.6333]); // Default to São Paulo
  const [zoomLevel, setZoomLevel] = useState(10);

  useEffect(() => {
    // Adjust map center and zoom if cameras array is not empty
    if (cameras && cameras.length > 0) {
      const latitudes = cameras.map(c => c.latitude).filter(lat => lat !== undefined) as number[];
      const longitudes = cameras.map(c => c.longitude).filter(lon => lon !== undefined) as number[];

      if (latitudes.length > 0 && longitudes.length > 0) {
        const avgLat = latitudes.reduce((sum, lat) => sum + lat, 0) / latitudes.length;
        const avgLon = longitudes.reduce((sum, lon) => sum + lon, 0) / longitudes.length;
        setMapCenter([avgLat, avgLon]);
        setZoomLevel(13); // Zoom in slightly if cameras are present
      }
    }
  }, [cameras]);

  return (
    <MapContainer center={mapCenter} zoom={zoomLevel} style={{ height: '500px', width: '100%' }} className="rounded-lg shadow-md">
      <TileLayer
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
      />
      <LayerGroup>
        {cameras.map((camera) => (
          <Marker
            key={camera.id}
            position={[camera.latitude, camera.longitude]}
            icon={camera.status === 'ONLINE' ? onlineIcon : offlineIcon}
          >
            <Popup>
              <strong>{camera.name}</strong><br />
              Status: {camera.status}
            </Popup>
            <Tooltip direction="top" offset={[0, -25]} opacity={1} permanent>
              {camera.name}
            </Tooltip>
          </Marker>
        ))}
      </LayerGroup>
    </MapContainer>
  );
}
