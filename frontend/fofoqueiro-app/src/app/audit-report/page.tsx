import { useState } from 'react';
import { AuditLog } from '@/domain/types';
import { useAuditLogFeatures } from '@/hooks/use-audit-features';
import { ColumnDef } from '@tanstack/react-table';
import { ArrowUpDown, Filter, X, FileText } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Calendar } from '@/components/ui/calendar';
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';
import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';
import MainLayout from '@/app/MainLayout';
import { toast } from 'sonner'; // Assuming toast is available

dayjs.extend(utc);

// Mock helper functions (replace with actual service calls if needed)
const getUserName = async (userId: string) => {
  // In a real app, fetch user data from API
  return `User ${userId.slice(-4)}`; // Mocking user display name
};

const getResourceName = async (resourceType: string, resourceId: string) => {
  if (resourceType === 'CAMERA') return `Camera ${resourceId.slice(-4)}`;
  if (resourceType === 'USER') return getUserName(resourceId);
  return resourceId; // Default to resourceId
};

type AuditLogFilters = {
  date: dayjs.Dayjs;
  action: string;
  resource: string;
  userId: string;
  ipAddress: string;
};

interface AuditLogPageProps {
  // Potentially pass initial filters or other props here
}

export default function AuditReportPage() {
  const queryClient = useQueryClient();
  const [filters, setFilters] = useState<AuditLogFilters>({
    date: dayjs.utc().startOf('day'), // Default to start of current day
    action: '',
    resource: '',
    userId: '',
    ipAddress: '',
  });

  const { auditLogs, isLoading, error, refetch, exportLogsMutation } = useAuditLogFeatures(filters);

  const handleFilterChange = (key: keyof AuditLogFilters, value: any) => {
    setFilters(prev => ({ ...prev, [key]: value }));
  };

  const clearFilters = () => {
    setFilters({
      date: dayjs.utc().startOf('day'),
      action: '',
      resource: '',
      userId: '',
      ipAddress: '',
    });
  };

  const handleExportPDF = () => {
    exportLogsMutation.mutate(filters); // Pass current filters to the mutation
  };

  const columns = auditLogColumns({
    filters,
    onFiltersChange: handleFilterChange,
    clearFilters: clearFilters,
  });

  return (
    <MainLayout>
      <div className="w-full p-6">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold">Relatório de Auditoria</h1>
          <div className="flex gap-2">
            <Button onClick={clearFilters} variant="outline">
              <X className="h-4 w-4 mr-2" /> Limpar Filtros
            </Button>
            <Button onClick={handleExportPDF} variant="secondary" disabled={exportLogsMutation.isPending}>
              {exportLogsMutation.isPending ? 'Exportando...' : (
                <>
                  <FileText className="h-4 w-4 mr-2" /> Exportar PDF
                </>
              )}
            </Button>
          </div>
        </div>

        {/* Filters Section */}
        <div className="mb-6 p-4 border rounded-md bg-card">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {/* Date Filter */}
            <div className="space-y-2">
              <Label>Data</Label>
              <Popover>
                <PopoverTrigger asChild>
                  <Button
                    variant={"outline"}
                    className="w-full justify-start text-left font-normal"
                  >
                    <CalendarIcon className="mr-2 h-4 w-4" />
                    {filters.date ? format(filters.date.toDate(), "PPP", { locale: ptBR }) : "Selecione uma data"}
                  </Button>
                </PopoverTrigger>
                <PopoverContent className="w-auto p-0" align="start">
                  <Calendar
                    mode="single"
                    selected={filters.date.toDate()}
                    onSelect={(date) => date && handleFilterChange('date', dayjs.utc(date).startOf('day'))}
                    initialFocus
                    locale={ptBR}
                  />
                </PopoverContent>
              </Popover>
            </div>

            {/* Action Filter */}
            <div className="space-y-2">
              <Label>Ação</Label>
              <Input
                placeholder="Ex: LOGIN, CREATE_CAMERA"
                value={filters.action}
                onChange={(e) => handleFilterChange('action', e.target.value)}
              />
            </div>

            {/* Resource Filter */}
            <div className="space-y-2">
              <Label>Recurso</Label>
              <Input
                placeholder="Ex: CAMERA, USER"
                value={filters.resource}
                onChange={(e) => handleFilterChange('resource', e.target.value)}
              />
            </div>

            {/* User ID Filter */}
            <div className="space-y-2">
              <Label>ID Usuário</Label>
              <Input
                placeholder="Ex: user-1"
                value={filters.userId}
                onChange={(e) => handleFilterChange('userId', e.target.value)}
              />
            </div>

            {/* IP Address Filter */}
            <div className="space-y-2">
              <Label>Endereço IP</Label>
              <Input
                placeholder="Ex: 192.168.1.100"
                value={filters.ipAddress}
                onChange={(e) => handleFilterChange('ipAddress', e.target.value)}
              />
            </div>
          </div>
        </div>

        {/* Data Table */}
        {isLoading && <p>Carregando logs de auditoria...</p>}
        {error && <p className="text-destructive">Erro ao carregar logs: {error.message}</p>}

        {auditLogs && (
          <DataTable columns={columns} data={auditLogs} />
        )}
      </div>
    </MainLayout>
  );
}
