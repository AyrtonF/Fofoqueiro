'use client';

import { useState, useEffect, useRef } from 'react';
import Hls from 'hls.js'; // Assuming hls.js is the chosen library for HLS playback

interface HlsPlayerProps {
  src: string; // URL of the HLS stream (.m3u8)
  cameraName: string;
  // Other props like controls, autoPlay, etc. can be added
}

export function HlsPlayer({ src, cameraName }: HlsPlayerProps) {
  const videoRef = useRef<HTMLVideoElement>(null);
  const hlsRef = useRef<Hls | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setIsLoading(true);
    setError(null);

    if (videoRef.current) {
      if (Hls.isSupported()) {
        const hls = new Hls({
          // HLS.js configuration options can go here
          // For example, for adaptive bitrate streaming:
          // enableWorker: true,
          // maxMaxBufferLength: 60,
          // maxBufferHole: 5,
        });
        hls.loadSource(src);
        hls.attachMedia(videoRef.current);

        hlsRef.current = hls;

        hls.on(Hls.Events.MEDIA_ATTACHED, () => {
          console.log('HLS player attached to media element');
        });

        hls.on(Hls.Events.MANIFEST_PARSED, () => {
          console.log('HLS manifest parsed');
          setIsLoading(false);
          // Optionally start playing automatically
          // videoRef.current?.play();
        });

        hls.on(Hls.Events.ERROR, (event, data) => {
          if (data.fatal) {
            console.error('HLS fatal error:', data);
            setError(`Erro ao carregar vídeo: ${data.type} - ${data.details}`);
            setIsLoading(false);
            // Handle retries or display error message
          } else {
            console.warn('HLS non-fatal error:', data);
          }
        });

      } else if (videoRef.current.canPlayType('application/vnd.apple.mpegurl')) {
        // Native HLS support (e.g., Safari)
        videoRef.current.src = src;
        videoRef.current.addEventListener('canplaythrough', () => {
          console.log('Native HLS player ready');
          setIsLoading(false);
        });
        videoRef.current.addEventListener('error', (e) => {
          console.error('Native HLS error:', e);
          setError('Erro ao carregar vídeo nativamente');
          setIsLoading(false);
        });
      } else {
        setError('Seu navegador não suporta HLS playback.');
        setIsLoading(false);
      }
    }

    return () => {
      // Cleanup
      if (hlsRef.current) {
        hlsRef.current.destroy();
        hlsRef.current = null;
      }
    };
  }, [src]); // Re-run effect if src changes

  return (
    <div className="relative w-full h-full bg-black rounded-lg overflow-hidden">
      {isLoading && (
        <div className="absolute inset-0 flex items-center justify-center bg-black/50 z-10">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
        </div>
      )}

      {error && (
        <div className="absolute inset-0 flex flex-col items-center justify-center bg-slate-900 text-white p-4 text-center">
          <p className="text-sm font-medium">{error}</p>
          {/* Add retry button if needed */}
        </div>
      )}

      <video
        ref={videoRef}
        controls // Show default video controls
        autoPlay={false} // Typically you'd want manual play for playback
        muted
        playsInline
        className="w-full h-full object-cover"
        title={`Player de vídeo para ${cameraName}`}
        // poster="path/to/poster.jpg" // Optional: display a poster image before playing
      />
    </div>
  );
}
