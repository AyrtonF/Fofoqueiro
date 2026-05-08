import { Camera } from '@/domain/types';
import { ColumnDef } from '@tanstack/react-table';
import { ArrowUpDown, MoreHorizontal, Video, Trash2 } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { CameraOff, CameraOn, AlertTriangle } from 'lucide-react';
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuTrigger } from '@/components/ui/dropdown-menu';
import Link from 'next/link';

// NOTE: Replace these with actual services to fetch real data
const getUserName = (userId: string) => `User ${userId.slice(-4)}`; 

interface CameraColumnsProps {
  onExpandCamera: (camera: Camera) => void;
  onEditCamera: (camera: Camera) => void;
  onDeleteCamera: (camera: Camera) => void;
}

export const cameraColumns = (
  queryClient: any, // Typed as any for simplicity, ideally QueryClient
  { onExpandCamera, onEditCamera, onDeleteCamera }: CameraColumnsProps
): ColumnDef<Camera>[] => {
  return [
    {
      accessorKey: 'name',
      header: ({ column }) => (
        <Button variant="ghost" onClick={() => column.toggleSorting(column.getIsSorted() === 'asc')}>
          Nome <ArrowUpDown className="ml-2 h-4 w-4" />
        </Button>
      ),
      cell: ({ row }) => <div className="capitalize">{row.getValue('name')}</div>,
    },
    {
      accessorKey: 'url',
      header: 'URL RTSP',
      cell: ({ row }) => <div className="text-xs truncate max-w-[150px]">{row.getValue('url')}</div>,
    },
    {
      accessorKey: 'status',
      header: ({ column }) => (
        <Button variant="ghost" onClick={() => column.toggleSorting(column.getIsSorted() === 'asc')}>
          Status <ArrowUpDown className="ml-2 h-4 w-4" />
        </Button>
      ),
      cell: ({ row }) => {
        const camera = row.original;
        const status = camera.status || 'OFFLINE';
        return (
          <div className="flex items-center">
            {status === 'ONLINE' ? (
              <CameraOn className="h-4 w-4 text-green-500" />
            ) : status === 'ERROR' ? (
              <AlertTriangle className="h-4 w-4 text-yellow-500" />
            ) : (
              <CameraOff className="h-4 w-4 text-red-500" />
            )}
            <span className="ml-2 text-sm capitalize">{status}</span>
          </div>
        );
      },
    },
    {
      accessorKey: 'gatewayId',
      header: 'Gateway ID',
      cell: ({ row }) => <div className="capitalize">{row.getValue('gatewayId')}</div>,
    },
    {
      id: 'actions',
      enableHiding: false,
      cell: ({ row }) => {
        const camera = row.original;
        return (
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost" className="h-8 w-8 p-0">
                <span className="sr-only">Menu</span>
                <MoreHorizontal className="h-4 w-4" />
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              <DropdownMenuLabel>Ações</DropdownMenuLabel>
              <DropdownMenuItem onClick={() => onExpandCamera(camera)}>
                Visualizar ao Vivo
              </DropdownMenuItem>
              <DropdownMenuItem asChild>
                <Link href={`/playback/${camera.id}`} className="flex items-center">
                  <Video className="h-4 w-4 mr-2" />
                  Visualizar Gravação
                </Link>
              </DropdownMenuItem>
              <DropdownMenuItem onClick={() => onEditCamera(camera)}>
                Editar
              </DropdownMenuItem>
              <DropdownMenuSeparator />
              <DropdownMenuItem onClick={() => onDeleteCamera(camera)} className="text-red-500">
                Deletar
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        );
      },
    },
  ];
};
