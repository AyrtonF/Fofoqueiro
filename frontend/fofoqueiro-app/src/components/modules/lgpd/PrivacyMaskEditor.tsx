'use client';

import React, { useRef, useState, useEffect, useCallback } from 'react';
import { Button } from '@/components/ui/button';
import { Label } from '@/components/ui/label';
import { Input } from '@/components/ui/input';
import { Card, CardContent, CardFooter, CardHeader, CardTitle, CardDescription } from '@/components/ui/card';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Eraser, Square, Circle, Trash2 } from 'lucide-react';
import { toast } from 'sonner'; // Assuming toast is available

// Define the shape of a mask point
interface MaskPoint {
  x: number;
  y: number;
}

// Define the shape of a privacy mask
interface PrivacyMaskShape {
  id: string;
  type: 'rectangle' | 'circle' | 'polygon';
  points: MaskPoint[];
  color?: string;
}

interface PrivacyMaskEditorProps {
  // imageUrl: string; // This should ideally be a dynamic source, e.g., from a camera snapshot or stream frame.
  // For now, we'll use a placeholder and add a comment.
  initialMasks?: PrivacyMaskShape[];
  onMasksChange: (masks: PrivacyMaskShape[]) => void;
  isEditing?: boolean;
}

export function PrivacyMaskEditor({
  initialMasks = [],
  onMasksChange,
  isEditing = true,
}: PrivacyMaskEditorProps) {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const imageRef = useRef<HTMLImageElement>(null); // To get image dimensions correctly
  const [masks, setMasks] = useState<PrivacyMaskShape[]>(initialMasks);
  const [currentTool, setCurrentTool] = useState<'rectangle' | 'circle' | 'polygon'>('rectangle');
  const [isDrawing, setIsDrawing] = useState(false);
  const [startPoint, setStartPoint] = useState<MaskPoint | null>(null);
  const [currentMask, setCurrentMask] = useState<PrivacyMaskShape | null>(null);
  const [imgWidth, setImgWidth] = useState(0);
  const [imgHeight, setImgHeight] = useState(0);

  // Placeholder for the image source. In a real application, this would be fetched dynamically,
  // e.g., from a camera snapshot API or a specific frame from the video stream.
  const placeholderImageUrl = '/images/privacy-mask-placeholder.png'; // Example placeholder image path

  // Load image and set canvas dimensions
  useEffect(() => {
    const img = new Image();
    img.src = placeholderImageUrl; // Use placeholder image
    img.onload = () => {
      if (canvasRef.current) {
        const canvas = canvasRef.current;
        canvas.width = img.width;
        canvas.height = img.height;
        setImgWidth(img.width);
        setImgHeight(img.height);
        drawImageOnCanvas(img, canvas, masks);
      }
      if (imageRef.current) {
        imageRef.current.src = placeholderImageUrl;
      }
    };
    img.onerror = () => {
      console.error("Failed to load image for mask editor.");
    };
  }, [placeholderImageUrl, masks]); // Re-draw if image source or masks change

  const drawImageOnCanvas = useCallback((img: HTMLImageElement, canvas: HTMLCanvasElement, currentMasks: PrivacyMaskShape[]) => {
    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.drawImage(img, 0, 0, canvas.width, canvas.height);

    currentMasks.forEach(mask => {
      ctx.fillStyle = mask.color || 'rgba(0, 0, 0, 0.7)';
      ctx.strokeStyle = 'rgba(255, 255, 255, 0.8)';
      ctx.lineWidth = 2;

      if (mask.type === 'rectangle' && mask.points.length >= 2) {
        const [p1, p2] = mask.points;
        const x = Math.min(p1.x, p2.x);
        const y = Math.min(p1.y, p2.y);
        const width = Math.abs(p1.x - p2.x);
        const height = Math.abs(p1.y - p2.y);
        ctx.fillRect(x, y, width, height);
        ctx.strokeRect(x, y, width, height);
      } else if (mask.type === 'circle' && mask.points.length >= 2) {
        const [center, pointOnEdge] = mask.points;
        const radius = Math.sqrt(Math.pow(pointOnEdge.x - center.x, 2) + Math.pow(pointOnEdge.y - center.y, 2));
        ctx.beginPath();
        ctx.arc(center.x, center.y, radius, 0, Math.PI * 2);
        ctx.fill();
        ctx.stroke();
      } else if (mask.type === 'polygon' && mask.points.length >= 3) {
        ctx.beginPath();
        ctx.moveTo(mask.points[0].x, mask.points[0].y);
        for (let i = 1; i < mask.points.length; i++) {
          ctx.lineTo(mask.points[i].x, mask.points[i].y);
        }
        ctx.closePath();
        ctx.fill();
        ctx.stroke();
      }
    });

    if (currentMask) {
      ctx.fillStyle = currentMask.color || 'rgba(0, 0, 0, 0.7)';
      ctx.strokeStyle = 'rgba(255, 255, 255, 0.8)';
      ctx.lineWidth = 2;

      if (currentMask.type === 'rectangle' && currentMask.points.length >= 2) {
        const [p1, p2] = currentMask.points;
        const x = Math.min(p1.x, p2.x);
        const y = Math.min(p1.y, p2.y);
        const width = Math.abs(p1.x - p2.x);
        const height = Math.abs(p1.y - p2.y);
        ctx.fillRect(x, y, width, height);
        ctx.strokeRect(x, y, width, height);
      } else if (currentMask.type === 'circle' && currentMask.points.length >= 2) {
        const [center, pointOnEdge] = currentMask.points;
        const radius = Math.sqrt(Math.pow(pointOnEdge.x - center.x, 2) + Math.pow(pointOnEdge.y - center.y, 2));
        ctx.beginPath();
        ctx.arc(center.x, center.y, radius, 0, Math.PI * 2);
        ctx.fill();
        ctx.stroke();
      } else if (currentMask.type === 'polygon' && currentMask.points.length >= 1) {
        ctx.beginPath();
        ctx.moveTo(currentMask.points[0].x, currentMask.points[0].y);
        for (let i = 1; i < currentMask.points.length; i++) {
          ctx.lineTo(currentMask.points[i].x, currentMask.points[i].y);
        }
        if (currentMask.points.length >= 3) {
          ctx.closePath();
        }
        ctx.fill();
        ctx.stroke();
      }
    }
  }, [masks, currentMask]);

  const getCanvasCoordinates = useCallback((event: React.MouseEvent<HTMLCanvasElement>): MaskPoint => {
    if (!canvasRef.current) return { x: 0, y: 0 };
    const rect = canvasRef.current.getBoundingClientRect();
    return {
      x: event.clientX - rect.left,
      y: event.clientY - rect.top,
    };
  }, []);

  const handleMouseDown = (event: React.MouseEvent<HTMLCanvasElement>) => {
    if (!isEditing) return;
    const point = getCanvasCoordinates(event);
    setStartPoint(point);
    setIsDrawing(true);
    setCurrentMask({ id: `mask-${Date.now()}`, type: currentTool, points: [point], color: 'rgba(0, 0, 0, 0.7)' });
  };

  const handleMouseMove = (event: React.MouseEvent<HTMLCanvasElement>) => {
    if (!isDrawing || !startPoint || !canvasRef.current || !currentMask) return;

    const currentPoint = getCanvasCoordinates(event);
    let updatedPoints: MaskPoint[];

    if (currentMask.type === 'rectangle' || currentMask.type === 'circle') {
      updatedPoints = [startPoint, currentPoint];
    } else { // Polygon drawing - add points
      updatedPoints = [...currentMask.points, currentPoint];
    }

    setCurrentMask({ ...currentMask, points: updatedPoints });
    if (canvasRef.current) {
      drawImageOnCanvas(imageRef.current!, canvasRef.current!, masks);
    }
  };

  const handleMouseUp = () => {
    if (!isDrawing || !currentMask || !canvasRef.current) return;

    setIsDrawing(false);
    setStartPoint(null);

    if (currentMask.points.length < (currentMask.type === 'polygon' ? 3 : 2)) {
      setCurrentMask(null);
      return;
    }

    const finalMasks = [...masks, currentMask];
    setMasks(finalMasks);
    onMasksChange(finalMasks);
    setCurrentMask(null);
  };

  const handleCanvasClick = (event: React.MouseEvent<HTMLCanvasElement>) => {
    if (currentTool === 'polygon' && !isDrawing) {
      const point = getCanvasCoordinates(event);
      if (!currentMask) {
        setCurrentMask({ id: `mask-${Date.now()}`, type: 'polygon', points: [point], color: 'rgba(0, 0, 0, 0.7)' });
        setIsDrawing(true);
      } else {
        setCurrentMask({ ...currentMask, points: [...currentMask.points, point] });
        if (canvasRef.current) {
          drawImageOnCanvas(imageRef.current!, canvasRef.current!, masks);
        }
      }
    } else if (currentTool === 'polygon' && isDrawing && currentMask) {
      if (currentMask.points.length > 0) {
        const start = currentMask.points[0];
        const clickPoint = getCanvasCoordinates(event);
        const distanceToStart = Math.sqrt(Math.pow(clickPoint.x - start.x, 2) + Math.pow(clickPoint.y - start.y, 2));
        if (distanceToStart < 15) {
          handleMouseUp();
        }
      }
    }
  };

  const handleToolChange = (tool: 'rectangle' | 'circle' | 'polygon') => {
    setCurrentTool(tool);
    if (isDrawing) {
      setIsDrawing(false);
      setCurrentMask(null);
    }
  };

  const removeMask = (idToRemove: string) => {
    const updatedMasks = masks.filter(mask => mask.id !== idToRemove);
    setMasks(updatedMasks);
    onMasksChange(updatedMasks);
  };

  const clearAllMasks = () => {
    setMasks([]);
    setCurrentMask(null);
    onMasksChange([]);
  };

  return (
    <Card className="w-full">
      <CardHeader>
        <CardTitle>Editor de Máscara de Privacidade</CardTitle>
        <CardDescription>Desenhe sobre a imagem para definir áreas de privacidade.</CardDescription>
      </CardHeader>
      <CardContent className="flex flex-col items-center">
        <div className="relative w-full max-w-3xl mx-auto mb-4">
          <canvas
            ref={canvasRef}
            className="block w-full h-auto border border-border rounded-md cursor-crosshair"
            onMouseDown={isEditing ? handleMouseDown : undefined}
            onMouseMove={isEditing ? handleMouseMove : undefined}
            onMouseUp={isEditing ? handleMouseUp : undefined}
            onClick={isEditing ? handleCanvasClick : undefined}
            style={{ backgroundColor: 'transparent' }}
          />
          {/* Hidden image to get dimensions */}
          <img ref={imageRef} alt="Video frame" className="hidden" onLoad={() => {}} />

          {/* Tool controls */}
          <div className="absolute top-2 left-2 flex gap-2 bg-black/50 p-2 rounded-md z-10">
            <Button
              variant={currentTool === 'rectangle' ? 'primary' : 'outline'}
              size="icon"
              onClick={() => handleToolChange('rectangle')}
              title="Retângulo"
              disabled={!isEditing}
            >
              <Square className="h-5 w-5" />
            </Button>
            <Button
              variant={currentTool === 'circle' ? 'primary' : 'outline'}
              size="icon"
              onClick={() => handleToolChange('circle')}
              title="Círculo"
              disabled={!isEditing}
            >
              <Circle className="h-5 w-5" />
            </Button>
            <Button
              variant={currentTool === 'polygon' ? 'primary' : 'outline'}
              size="icon"
              onClick={() => handleToolChange('polygon')}
              title="Polígono"
              disabled={!isEditing}
            >
              <Eraser className="h-5 w-5" />
            </Button>
            <Button
              variant="outline"
              size="icon"
              onClick={clearAllMasks}
              title="Limpar todas as máscaras"
              disabled={!isEditing || masks.length === 0}
            >
              <Trash2 className="h-5 w-5" />
            </Button>
          </div>
        </div>

        {/* List of applied masks */}
        {masks.length > 0 && (
          <div className="mt-4 w-full max-w-3xl">
            <h3 className="text-lg font-semibold mb-2">Máscaras Aplicadas</h3>
            <div className="border rounded-md p-3 bg-card">
              {masks.map((mask, index) => (
                <div key={mask.id} className="flex items-center justify-between mb-2 p-2 border-b last:border-b-0">
                  <span className="text-sm">
                    {mask.type.charAt(0).toUpperCase() + mask.type.slice(1)} #{index + 1} ({mask.points.length} pts)
                  </span>
                  <Button variant="outline" size="sm" onClick={() => removeMask(mask.id)}>
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </div>
              ))}
            </div>
          </div>
        )}
      </CardContent>
      <CardFooter className="flex justify-end">
        <Button onClick={() => onMasksChange(masks)} disabled={masks.length === 0 || !isEditing}>
          Salvar Máscaras
        </Button>
      </CardFooter>
    </Card>
  );
}
