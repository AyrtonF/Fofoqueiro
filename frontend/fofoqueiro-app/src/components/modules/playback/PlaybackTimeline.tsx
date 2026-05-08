import { useState, useEffect, useRef } from 'react';
import { Camera, Recording } from '@/domain/types';
import { cameraService } from '@/services/camera-service';
import { HlsPlayer } from './HlsPlayer'; // Assuming HlsPlayer is in the same directory or aliased
import { Button } from '@/components/ui/button';
import { CalendarRange, Clock, ChevronLeft, ChevronRight, CalendarIcon } from 'lucide-react';
import dayjs from 'dayjs'; // For date/time manipulation
import utc from 'dayjs/plugin/utc';
import timezone from 'dayjs/plugin/timezone';
import customParseFormat from 'dayjs/plugin/customParseFormat';

// Extend dayjs with necessary plugins
dayjs.extend(utc);
dayjs.extend(timezone);
dayjs.extend(customParseFormat);
dayjs.tz.setDefault('UTC'); // Set default timezone for parsing if backend doesn't specify

interface PlaybackTimelineProps {
  camera: Camera;
}

export function PlaybackTimeline({ camera }: PlaybackTimelineProps) {
  const [selectedDate, setSelectedDate] = useState<dayjs.Dayjs>(dayjs.utc());
  const [recordings, setRecordings] = useState<Recording[]>([]);
  const [isLoadingRecordings, setIsLoadingRecordings] = useState(false);
  const [selectedRecording, setSelectedRecording] = useState<Recording | null>(null);
  const [currentTime, setCurrentTime] = useState<dayjs.Dayjs>(dayjs.utc()); // State for current playback time
  const [isScrubbing, setIsScrubbing] = useState(false); // For timeline interaction

  // Fetch recordings for the selected camera and date
  useEffect(() => {
    const fetchRecordings = async () => {
      setIsLoadingRecordings(true);
      try {
        // Mock API call - replace with actual service call
        // const response = await cameraService.getRecordings(camera.id, selectedDate.format('YYYY-MM-DD'));
        // setRecordings(response);
        
        // Mock data for now
        const mockRecordings: Recording[] = [
          {
            id: 'rec-1',
            cameraId: camera.id,
            startTime: dayjs.utc(selectedDate).set('hour', 9).set('minute', 0).toISOString(),
            endTime: dayjs.utc(selectedDate).set('hour', 10).set('minute', 30).toISOString(),
            s3Path: '/mock/path/to/recording1.m3u8', // Use .m3u8 for HLS
            size: 1000000,
          },
          {
            id: 'rec-2',
            cameraId: camera.id,
            startTime: dayjs.utc(selectedDate).set('hour', 11).set('minute', 15).toISOString(),
            endTime: dayjs.utc(selectedDate).set('hour', 12).set('minute', 0).toISOString(),
            s3Path: '/mock/path/to/recording2.m3u8',
            size: 500000,
          },
        ];
        setRecordings(mockRecordings);
        // Select the first recording by default if available
        if (mockRecordings.length > 0) {
          setSelectedRecording(mockRecordings[0]);
        } else {
          setSelectedRecording(null);
        }
      } catch (err) {
        console.error("Error fetching recordings:", err);
        setRecordings([]);
        setSelectedRecording(null);
      } finally {
        setIsLoadingRecordings(false);
      }
    };

    fetchRecordings();
  }, [camera.id, selectedDate]);

  const handleDateChange = (direction: 'prev' | 'next') => {
    setSelectedDate(prev => direction === 'prev' ? prev.subtract(1, 'day') : prev.add(1, 'day'));
  };

  // --- Timeline Logic (Simplified for now) ---
  const timelineWidth = 720; // px, representing 24 hours * 30px/hour (example)
  const hoursInDay = 24;
  const pxPerHour = timelineWidth / hoursInDay;

  const getXPositionForTime = (time: dayjs.Dayjs): number => {
    const hoursSinceMidnight = time.diff(dayjs.utc(time).startOf('day'), 'hour', true);
    return hoursSinceMidnight * pxPerHour;
  };

  const handleTimelineClick = (event: React.MouseEvent<HTMLDivElement>) => {
    const rect = event.currentTarget.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const hours = x / pxPerHour;
    const newTime = dayjs.utc(selectedDate).startOf('day').add(hours, 'hour');
    setCurrentTime(newTime);
    // In a real player, this would seek to the new time
  };

  const formatTime = (time: dayjs.Dayjs) => {
    return time.format('HH:mm:ss');
  };

  return (
    <div className="flex flex-col w-full h-full p-4">
      {/* Date Navigation and Camera Selection */}
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-2">
          <Button variant="outline" size="icon" onClick={() => handleDateChange('prev')}>
            <ChevronLeft className="h-4 w-4" />
          </Button>
          <h2 className="text-lg font-semibold">
            {selectedDate.format('DD/MM/YYYY')}
          </h2>
          <Button variant="outline" size="icon" onClick={() => handleDateChange('next')}>
            <ChevronRight className="h-4 w-4" />
          </Button>
        </div>
        <div className="flex items-center gap-2">
          <span className="text-sm font-medium">Câmera: {camera.name}</span>
          {/* Add camera selection dropdown here if multiple cameras */}
        </div>
      </div>

      {/* Video Player Area */}
      <div className="flex-1 relative mb-4">
        {selectedRecording ? (
          <HlsPlayer src={selectedRecording.s3Path} cameraName={camera.name} />
        ) : (
          <div className="flex items-center justify-center h-full w-full bg-black rounded-lg text-white">
            {isLoadingRecordings ? 'Carregando gravações...' : 'Selecione uma gravação para visualizar'}
          </div>
        )}
      </div>

      {/* Timeline and Controls */}
      <div className="w-full bg-card rounded-lg p-4">
        <div className="flex items-center justify-between mb-2">
          <div className="flex items-center gap-4">
            <span className="text-sm font-medium">{formatTime(currentTime)}</span>
            {/* Play/Pause buttons would go here */}
          </div>
          <div className="flex items-center gap-2">
            <Button variant="ghost" size="sm">Anterior</Button> {/* For previous recording */}
            <Button variant="ghost" size="sm">Próximo</Button> {/* For next recording */}
          </div>
        </div>

        {/* Timeline */}
        <div
          className="relative w-full h-8 bg-border rounded-lg cursor-pointer select-none"
          onClick={handleTimelineClick}
          onMouseDown={() => setIsScrubbing(true)}
          onMouseUp={() => setIsScrubbing(false)}
          onMouseMove={(e) => {
            if (isScrubbing) {
              handleTimelineClick(e as any); // Type assertion for simplicity
            }
          }}
          onMouseLeave={() => setIsScrubbing(false)} // Stop scrubbing if mouse leaves
        >
          {/* Time scale markers */}
          <div className="absolute inset-0 flex justify-between items-center px-2 text-xs text-muted-foreground">
            <span>00:00:00</span>
            <span>12:00:00</span>
            <span>23:59:59</span>
          </div>

          {/* Recording segments */}
          {recordings.map(rec => {
            const startPos = getXPositionForTime(dayjs.utc(rec.startTime));
            const endPos = getXPositionForTime(dayjs.utc(rec.endTime));
            const durationPx = endPos - startPos;
            return (
              <div
                key={rec.id}
                className="absolute bg-primary/50 h-full rounded-lg hover:bg-primary/70 transition-colors"
                style={{ left: `${startPos}px`, width: `${durationPx}px` }}
                title={`${formatTime(dayjs.utc(rec.startTime))} - ${formatTime(dayjs.utc(rec.endTime))}`}
                onClick={(e) => {
                  e.stopPropagation(); // Prevent timeline click from triggering
                  setSelectedRecording(rec);
                  setCurrentTime(dayjs.utc(rec.startTime));
                }}
              />
            );
          })}

          {/* Current time indicator */}
          <div
            className="absolute top-0 h-full w-0.5 bg-red-500 transition-none" // No transition for real-time movement
            style={{ left: `${getXPositionForTime(currentTime)}px` }}
          />
        </div>
      </div>
    </div>
  );
}
