import { ColumnDef } from '@tanstack/react-table';
import { AuditLog } from '@/domain/types';
import { Button } from '@/components/ui/button';
import { ArrowUpDown } from 'lucide-react';
import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';

dayjs.extend(utc);

export const auditLogColumns: ColumnDef<AuditLog>[] = [
  {
    accessorKey: 'timestamp',
    header: ({ column }) => (
      <Button variant="ghost" onClick={() => column.toggleSorting(column.getIsSorted() === 'asc')}>
        Data <ArrowUpDown className="ml-2 h-4 w-4" />
      </Button>
    ),
    cell: ({ row }) => dayjs.utc(row.getValue('timestamp')).format('DD/MM/YYYY HH:mm'),
  },
  { accessorKey: 'action', header: 'Ação' },
  { accessorKey: 'resource', header: 'Recurso' },
  { accessorKey: 'userId', header: 'Usuário' },
  { accessorKey: 'ipAddress', header: 'IP' },
];
