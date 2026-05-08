import { AuditLog } from '@/domain/types';
import { auditService } from '@/services/audit-service';
import { useQueryClient, useQuery, useMutation } from '@tanstack/react-query';
import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';
import { toast } from 'sonner';

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

export function useAuditLogFeatures() {
  const queryClient = useQueryClient();

  // Query to fetch audit logs with filters
  const fetchAuditLogs = (filters: AuditLogFilters) => useQuery<AuditLog[]>({
    queryKey: ['audit-logs', filters],
    queryFn: () => auditService.getAuditLogs(filters),
    staleTime: 5 * 60 * 1000, // Data is considered fresh for 5 minutes
    enabled: !!filters,
  });

  // Mutation for exporting logs
  const exportLogs = useMutation({
    mutationFn: (filters: AuditLogFilters) => auditService.exportAuditLogs(filters), // Pass current filters
    onSuccess: (blob: Blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `audit-logs-${dayjs.utc().format('YYYY-MM-DD')}.pdf`; // Filename for download
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);
      toast.success('Relatório exportado com sucesso!');
    },
    onError: (error: any) => {
      toast.error(`Erro ao exportar relatório: ${error.message || 'Erro desconhecido'}`);
    },
  });

  return {
    fetchAuditLogs,
    exportLogsMutation: exportLogs,
  };
}
