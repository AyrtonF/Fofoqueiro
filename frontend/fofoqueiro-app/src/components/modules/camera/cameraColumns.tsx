import { Camera } from '@/domain/types';
import { ColumnDef } from '@tanstack/react-table';
import { ArrowUpDown, AlertTriangle, Camera as CameraIcon } from 'lucide-react';
import { Button } from '@/components/ui/button';

export const cameraColumns: ColumnDef<Camera>[] = [
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
      const status = row.original.status || 'OFFLINE';
      return (
        <div className="flex items-center">
          {status === 'ONLINE' ? (
            <CameraIcon className="h-4 w-4 text-green-500" />
          ) : status === 'ERROR' ? (
            <AlertTriangle className="h-4 w-4 text-yellow-500" />
          ) : (
            <CameraIcon className="h-4 w-4 text-red-500" />
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
];
