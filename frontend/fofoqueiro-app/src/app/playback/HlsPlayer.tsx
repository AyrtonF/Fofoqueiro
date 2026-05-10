import React from 'react';

export function HlsPlayer({ src }: { src?: string }) {
  return (
    <div className="bg-black text-white p-4">HLS Player placeholder for {src}</div>
  );
}

export default HlsPlayer;
