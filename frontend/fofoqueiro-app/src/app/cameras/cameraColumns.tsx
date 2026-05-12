"use client";

import { Camera } from '@/domain/types';
import { ColumnDef } from '@tanstack/react-table';
import { ArrowUpDown, Camera as CameraIcon, AlertTriangle, Eye, Pencil, Trash2 } from 'lucide-react';
import { Button } from '@/components/ui/button';

type CameraActions = {
	onExpand: (camera: Camera) => void;
	onEdit: (camera: Camera) => void;
	onDelete: (camera: Camera) => void;
};

export const createCameraColumns = ({ onExpand, onEdit, onDelete }: CameraActions): ColumnDef<Camera>[] => [
	{
		accessorKey: 'name',
		header: ({ column }) => (
			<Button variant="ghost" onClick={() => column.toggleSorting(column.getIsSorted() === 'asc')}>
				Nome <ArrowUpDown className="ml-2 h-4 w-4" />
			</Button>
		),
		cell: ({ row }) => <div className="font-medium capitalize">{row.getValue('name')}</div>,
	},
	{
		accessorKey: 'url',
		header: 'URL RTSP',
		cell: ({ row }) => <div className="max-w-[240px] truncate text-xs text-muted-foreground">{row.getValue('url')}</div>,
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
				<div className="flex items-center gap-2">
					{status === 'ONLINE' ? (
						<CameraIcon className="h-4 w-4 text-emerald-500" />
					) : status === 'ERROR' ? (
						<AlertTriangle className="h-4 w-4 text-amber-500" />
					) : (
						<CameraIcon className="h-4 w-4 text-rose-500" />
					)}
					<span className="text-sm font-medium capitalize">{status}</span>
				</div>
			);
		},
	},
	{
		accessorKey: 'gatewayId',
		header: 'Gateway',
		cell: ({ row }) => <div className="text-sm text-muted-foreground">{row.getValue('gatewayId')}</div>,
	},
	{
		id: 'actions',
		header: 'Ações',
		cell: ({ row }) => {
			const camera = row.original;

			return (
				<div className="flex items-center gap-1">
					<Button variant="ghost" size="icon" onClick={() => onExpand(camera)} aria-label={`Expandir ${camera.name}`}>
						<Eye className="h-4 w-4" />
					</Button>
					<Button variant="ghost" size="icon" onClick={() => onEdit(camera)} aria-label={`Editar ${camera.name}`}>
						<Pencil className="h-4 w-4" />
					</Button>
					<Button variant="ghost" size="icon" onClick={() => onDelete(camera)} aria-label={`Excluir ${camera.name}`}>
						<Trash2 className="h-4 w-4" />
					</Button>
				</div>
			);
		},
	},
];

export const cameraColumns = createCameraColumns;

export default createCameraColumns;
