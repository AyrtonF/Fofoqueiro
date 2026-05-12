"use client";

import { FormEvent, useEffect, useMemo, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { Settings, Plus, Pencil, Trash2, Save, BellRing, ShieldCheck, Users } from 'lucide-react';
import { toast } from 'sonner';
import { useAuthStore } from '@/store/auth-store';
import { useTenantConfig } from '@/hooks/use-tenant-config';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { tenantService, TenantRequest, WhiteLabelConfigRequest } from '@/services/tenant-service';
import { healthMetricService, HealthMetricRequest } from '@/services/health-metric-service';
import { userSessionService, UserSessionRequest } from '@/services/user-session-service';
import { Tenant, WhiteLabelConfig, HealthMetric, UserSession } from '@/domain/types';

const nowToLocalValue = () => new Date().toISOString().slice(0, 16);

function toLocalValue(value?: string) {
  return value ? new Date(value).toISOString().slice(0, 16) : nowToLocalValue();
}

export default function SettingsPage() {
  const queryClient = useQueryClient();
  const currentTenantId = useAuthStore((state) => state.user?.tenantId);
  const { config: currentConfig } = useTenantConfig();

  const [tenantForm, setTenantForm] = useState<TenantRequest>({ name: '', domain: '', isActive: true });
  const [editingTenantId, setEditingTenantId] = useState<string | null>(null);

  const [whiteLabelForm, setWhiteLabelForm] = useState<WhiteLabelConfigRequest>({
    tenantId: currentTenantId || '',
    logoUrl: '',
    primaryColor: '#3b82f6',
    secondaryColor: '#0f172a',
    faviconUrl: '',
  });

  const [editingMetricId, setEditingMetricId] = useState<string | null>(null);
  const [metricForm, setMetricForm] = useState<HealthMetricRequest>({
    tenantId: currentTenantId || '',
    cameraId: '',
    online: true,
    fps: 0,
    bitrate: 0,
    recordingConfidence: 0,
    measuredAt: nowToLocalValue(),
  });

  const [editingSessionId, setEditingSessionId] = useState<string | null>(null);
  const [sessionForm, setSessionForm] = useState<UserSessionRequest>({
    tenantId: currentTenantId || '',
    userId: '',
    tokenId: '',
    expiresAt: nowToLocalValue(),
    lastActivityAt: nowToLocalValue(),
  });

  useEffect(() => {
    if (currentTenantId) {
      setWhiteLabelForm((prev) => ({ ...prev, tenantId: currentTenantId }));
      setMetricForm((prev) => ({ ...prev, tenantId: currentTenantId }));
      setSessionForm((prev) => ({ ...prev, tenantId: currentTenantId }));
    }
  }, [currentTenantId]);

  useEffect(() => {
    if (currentConfig) {
      setWhiteLabelForm({
        tenantId: currentConfig.tenantId || currentTenantId || '',
        logoUrl: currentConfig.logoUrl || '',
        primaryColor: currentConfig.primaryColor || '#3b82f6',
        secondaryColor: currentConfig.secondaryColor || '#0f172a',
        faviconUrl: currentConfig.faviconUrl || '',
      });
    }
  }, [currentConfig, currentTenantId]);

  const tenantsQuery = useQuery({
    queryKey: ['tenants'],
    queryFn: tenantService.list,
  });

  const whiteLabelConfigsQuery = useQuery({
    queryKey: ['white-label-configs'],
    queryFn: tenantService.listWhiteLabelConfigs,
  });

  const metricsQuery = useQuery({
    queryKey: ['health-metrics'],
    queryFn: () => healthMetricService.list(),
  });

  const sessionsQuery = useQuery({
    queryKey: ['user-sessions'],
    queryFn: () => userSessionService.list(),
  });

  const saveTenantMutation = useMutation({
    mutationFn: async () => {
      if (editingTenantId) {
        return tenantService.update(editingTenantId, tenantForm);
      }
      return tenantService.create(tenantForm);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['tenants'] });
      setTenantForm({ name: '', domain: '', isActive: true });
      setEditingTenantId(null);
      toast.success(editingTenantId ? 'Tenant atualizado.' : 'Tenant criado.');
    },
    onError: (error: any) => toast.error(error?.message || 'Falha ao salvar tenant.'),
  });

  const deleteTenantMutation = useMutation({
    mutationFn: (id: string) => tenantService.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['tenants'] });
      toast.success('Tenant removido.');
    },
    onError: (error: any) => toast.error(error?.message || 'Falha ao remover tenant.'),
  });

  const saveWhiteLabelMutation = useMutation({
    mutationFn: async () => {
      if (currentConfig?.id) {
        return tenantService.updateWhiteLabelConfig(String(currentConfig.id), whiteLabelForm);
      }
      return tenantService.createWhiteLabelConfig(whiteLabelForm);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['tenant-config'] });
      queryClient.invalidateQueries({ queryKey: ['white-label-configs'] });
      toast.success('Configuração visual salva.');
    },
    onError: (error: any) => toast.error(error?.message || 'Falha ao salvar configuração visual.'),
  });

  const saveMetricMutation = useMutation({
    mutationFn: async () => {
      if (editingMetricId) {
        return healthMetricService.update(editingMetricId, metricForm);
      }
      return healthMetricService.create(metricForm);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['health-metrics'] });
      setEditingMetricId(null);
      setMetricForm((prev) => ({ ...prev, cameraId: '', online: true, fps: 0, bitrate: 0, recordingConfidence: 0, measuredAt: nowToLocalValue() }));
      toast.success(editingMetricId ? 'Métrica atualizada.' : 'Métrica criada.');
    },
    onError: (error: any) => toast.error(error?.message || 'Falha ao salvar métrica.'),
  });

  const deleteMetricMutation = useMutation({
    mutationFn: (id: string) => healthMetricService.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['health-metrics'] });
      toast.success('Métrica removida.');
    },
    onError: (error: any) => toast.error(error?.message || 'Falha ao remover métrica.'),
  });

  const saveSessionMutation = useMutation({
    mutationFn: async () => {
      if (editingSessionId) {
        return userSessionService.update(editingSessionId, sessionForm);
      }
      return userSessionService.create(sessionForm);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['user-sessions'] });
      setEditingSessionId(null);
      setSessionForm((prev) => ({ ...prev, userId: '', tokenId: '', expiresAt: nowToLocalValue(), lastActivityAt: nowToLocalValue() }));
      toast.success(editingSessionId ? 'Sessão atualizada.' : 'Sessão criada.');
    },
    onError: (error: any) => toast.error(error?.message || 'Falha ao salvar sessão.'),
  });

  const deleteSessionMutation = useMutation({
    mutationFn: (id: string) => userSessionService.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['user-sessions'] });
      toast.success('Sessão removida.');
    },
    onError: (error: any) => toast.error(error?.message || 'Falha ao remover sessão.'),
  });

  const editingTenant = useMemo(() => {
    return tenantsQuery.data?.find((tenant) => tenant.id === editingTenantId) ?? null;
  }, [editingTenantId, tenantsQuery.data]);

  useEffect(() => {
    if (editingTenant) {
      setTenantForm({
        name: editingTenant.name,
        domain: editingTenant.domain,
        isActive: editingTenant.isActive ?? true,
      });
    }
  }, [editingTenant]);

  const submitTenant = (event: FormEvent) => {
    event.preventDefault();
    saveTenantMutation.mutate();
  };

  const submitWhiteLabel = (event: FormEvent) => {
    event.preventDefault();
    saveWhiteLabelMutation.mutate();
  };

  const submitMetric = (event: FormEvent) => {
    event.preventDefault();
    saveMetricMutation.mutate();
  };

  const submitSession = (event: FormEvent) => {
    event.preventDefault();
    saveSessionMutation.mutate();
  };

  return (
    <div className="w-full p-6 space-y-6">
      <div className="rounded-2xl border border-border bg-card p-6">
        <div className="flex flex-wrap items-start justify-between gap-4">
          <div>
            <div className="mb-2 inline-flex items-center gap-2 rounded-full border border-primary/20 bg-primary/10 px-3 py-1 text-xs font-medium text-primary">
              <Settings className="h-3.5 w-3.5" />
              Administração
            </div>
            <h1 className="text-3xl font-bold tracking-tight">Configurações do Fofoqueiro</h1>
            <p className="mt-2 max-w-2xl text-sm text-muted-foreground">
              Central de gestão do tenant, identidade visual e registros operacionais do backend.
            </p>
          </div>
          <div className="grid gap-2 text-right text-sm text-muted-foreground">
            <span>Tenant atual: {currentTenantId || 'não identificado'}</span>
            <span>Configuração visual: {currentConfig?.primaryColor || 'pendente'}</span>
          </div>
        </div>
      </div>

      <div className="grid gap-6 xl:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2 text-xl"><Users className="h-5 w-5" /> Tenants</CardTitle>
            <CardDescription>Crie, edite e remova tenants do sistema.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <form onSubmit={submitTenant} className="grid gap-4 rounded-xl border border-border/60 p-4">
              <div className="grid gap-2">
                <Label htmlFor="tenant-name">Nome</Label>
                <Input id="tenant-name" value={tenantForm.name} onChange={(event) => setTenantForm((prev) => ({ ...prev, name: event.target.value }))} />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="tenant-domain">Domínio</Label>
                <Input id="tenant-domain" value={tenantForm.domain} onChange={(event) => setTenantForm((prev) => ({ ...prev, domain: event.target.value }))} />
              </div>
              <label className="flex items-center gap-2 text-sm">
                <input
                  type="checkbox"
                  checked={tenantForm.isActive}
                  onChange={(event) => setTenantForm((prev) => ({ ...prev, isActive: event.target.checked }))}
                />
                Tenant ativo
              </label>
              <div className="flex items-center justify-end gap-2">
                {editingTenantId && (
                  <Button type="button" variant="outline" onClick={() => { setEditingTenantId(null); setTenantForm({ name: '', domain: '', isActive: true }); }}>
                    Cancelar
                  </Button>
                )}
                <Button type="submit" disabled={saveTenantMutation.isPending}>
                  {editingTenantId ? <Save className="mr-2 h-4 w-4" /> : <Plus className="mr-2 h-4 w-4" />}
                  {editingTenantId ? 'Salvar' : 'Criar'}
                </Button>
              </div>
            </form>

            <div className="overflow-x-auto rounded-xl border border-border/60">
              <table className="w-full text-sm">
                <thead className="bg-muted/40 text-left text-muted-foreground">
                  <tr>
                    <th className="px-3 py-2">Nome</th>
                    <th className="px-3 py-2">Domínio</th>
                    <th className="px-3 py-2">Ativo</th>
                    <th className="px-3 py-2 text-right">Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {tenantsQuery.data?.length ? tenantsQuery.data.map((tenant: Tenant) => (
                    <tr key={tenant.id} className="border-t border-border/60">
                      <td className="px-3 py-2 font-medium">{tenant.name}</td>
                      <td className="px-3 py-2 text-muted-foreground">{tenant.domain}</td>
                      <td className="px-3 py-2">{tenant.isActive ? 'Sim' : 'Não'}</td>
                      <td className="px-3 py-2">
                        <div className="flex justify-end gap-2">
                          <Button variant="ghost" size="sm" onClick={() => setEditingTenantId(String(tenant.id))}>
                            <Pencil className="h-4 w-4" />
                          </Button>
                          <Button variant="ghost" size="sm" onClick={() => deleteTenantMutation.mutate(String(tenant.id))}>
                            <Trash2 className="h-4 w-4" />
                          </Button>
                        </div>
                      </td>
                    </tr>
                  )) : (
                    <tr>
                      <td className="px-3 py-6 text-center text-muted-foreground" colSpan={4}>Nenhum tenant encontrado.</td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2 text-xl"><ShieldCheck className="h-5 w-5" /> White Label</CardTitle>
            <CardDescription>Atualize as cores e marcas do tenant atual.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <form onSubmit={submitWhiteLabel} className="grid gap-4 rounded-xl border border-border/60 p-4">
              <div className="grid gap-2">
                <Label htmlFor="white-logo">Logo URL</Label>
                <Input id="white-logo" value={whiteLabelForm.logoUrl || ''} onChange={(event) => setWhiteLabelForm((prev) => ({ ...prev, logoUrl: event.target.value }))} />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="white-favicon">Favicon URL</Label>
                <Input id="white-favicon" value={whiteLabelForm.faviconUrl || ''} onChange={(event) => setWhiteLabelForm((prev) => ({ ...prev, faviconUrl: event.target.value }))} />
              </div>
              <div className="grid gap-4 sm:grid-cols-2">
                <div className="grid gap-2">
                  <Label htmlFor="white-primary">Cor primária</Label>
                  <Input id="white-primary" value={whiteLabelForm.primaryColor || ''} onChange={(event) => setWhiteLabelForm((prev) => ({ ...prev, primaryColor: event.target.value }))} />
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="white-secondary">Cor secundária</Label>
                  <Input id="white-secondary" value={whiteLabelForm.secondaryColor || ''} onChange={(event) => setWhiteLabelForm((prev) => ({ ...prev, secondaryColor: event.target.value }))} />
                </div>
              </div>
              <div className="flex items-center justify-end gap-2">
                <Button type="submit" disabled={saveWhiteLabelMutation.isPending}>
                  <Save className="mr-2 h-4 w-4" />
                  Salvar identidade
                </Button>
              </div>
            </form>

            <div className="overflow-x-auto rounded-xl border border-border/60">
              <table className="w-full text-sm">
                <thead className="bg-muted/40 text-left text-muted-foreground">
                  <tr>
                    <th className="px-3 py-2">Tenant</th>
                    <th className="px-3 py-2">Primária</th>
                    <th className="px-3 py-2">Secundária</th>
                    <th className="px-3 py-2">Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {whiteLabelConfigsQuery.data?.length ? whiteLabelConfigsQuery.data.map((config: WhiteLabelConfig) => (
                    <tr key={config.id ?? config.tenantId} className="border-t border-border/60">
                      <td className="px-3 py-2">{config.tenantId}</td>
                      <td className="px-3 py-2">{config.primaryColor}</td>
                      <td className="px-3 py-2">{config.secondaryColor}</td>
                      <td className="px-3 py-2">
                        <div className="flex gap-2">
                          <Button variant="ghost" size="sm" onClick={() => config.id && setWhiteLabelForm({
                            tenantId: config.tenantId || currentTenantId || '',
                            logoUrl: config.logoUrl || '',
                            primaryColor: config.primaryColor || '#3b82f6',
                            secondaryColor: config.secondaryColor || '#0f172a',
                            faviconUrl: config.faviconUrl || '',
                          })}>
                            <Pencil className="h-4 w-4" />
                          </Button>
                          {config.id && (
                            <Button variant="ghost" size="sm" onClick={() => tenantService.deleteWhiteLabelConfig(String(config.id)).then(() => queryClient.invalidateQueries({ queryKey: ['white-label-configs'] }))}>
                              <Trash2 className="h-4 w-4" />
                            </Button>
                          )}
                        </div>
                      </td>
                    </tr>
                  )) : (
                    <tr>
                      <td className="px-3 py-6 text-center text-muted-foreground" colSpan={4}>Nenhuma configuração encontrada.</td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </CardContent>
        </Card>
      </div>

      <div className="grid gap-6 xl:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2 text-xl"><BellRing className="h-5 w-5" /> Health Metrics</CardTitle>
            <CardDescription>Cadastre e acompanhe telemetria de saúde das câmeras.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <form onSubmit={submitMetric} className="grid gap-4 rounded-xl border border-border/60 p-4">
              <div className="grid gap-4 sm:grid-cols-2">
                <div className="grid gap-2">
                  <Label htmlFor="metric-camera">Camera ID</Label>
                  <Input id="metric-camera" value={metricForm.cameraId} onChange={(event) => setMetricForm((prev) => ({ ...prev, cameraId: event.target.value }))} />
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="metric-measured">Medido em</Label>
                  <Input id="metric-measured" type="datetime-local" value={metricForm.measuredAt || ''} onChange={(event) => setMetricForm((prev) => ({ ...prev, measuredAt: event.target.value }))} />
                </div>
              </div>
              <label className="flex items-center gap-2 text-sm">
                <input type="checkbox" checked={metricForm.online} onChange={(event) => setMetricForm((prev) => ({ ...prev, online: event.target.checked }))} />
                Online
              </label>
              <div className="grid gap-4 sm:grid-cols-3">
                <div className="grid gap-2">
                  <Label htmlFor="metric-fps">FPS</Label>
                  <Input id="metric-fps" type="number" value={metricForm.fps ?? ''} onChange={(event) => setMetricForm((prev) => ({ ...prev, fps: Number(event.target.value) }))} />
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="metric-bitrate">Bitrate</Label>
                  <Input id="metric-bitrate" type="number" value={metricForm.bitrate ?? ''} onChange={(event) => setMetricForm((prev) => ({ ...prev, bitrate: Number(event.target.value) }))} />
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="metric-confidence">Confiança</Label>
                  <Input id="metric-confidence" type="number" step="any" value={metricForm.recordingConfidence ?? ''} onChange={(event) => setMetricForm((prev) => ({ ...prev, recordingConfidence: Number(event.target.value) }))} />
                </div>
              </div>
              <div className="flex items-center justify-end gap-2">
                {editingMetricId && (
                  <Button type="button" variant="outline" onClick={() => { setEditingMetricId(null); setMetricForm({ tenantId: currentTenantId || '', cameraId: '', online: true, fps: 0, bitrate: 0, recordingConfidence: 0, measuredAt: nowToLocalValue() }); }}>
                    Cancelar
                  </Button>
                )}
                <Button type="submit" disabled={saveMetricMutation.isPending}>
                  {editingMetricId ? <Save className="mr-2 h-4 w-4" /> : <Plus className="mr-2 h-4 w-4" />}
                  {editingMetricId ? 'Atualizar métrica' : 'Registrar métrica'}
                </Button>
              </div>
            </form>

            <div className="overflow-x-auto rounded-xl border border-border/60">
              <table className="w-full text-sm">
                <thead className="bg-muted/40 text-left text-muted-foreground">
                  <tr>
                    <th className="px-3 py-2">Camera</th>
                    <th className="px-3 py-2">Online</th>
                    <th className="px-3 py-2">Medido em</th>
                    <th className="px-3 py-2 text-right">Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {metricsQuery.data?.length ? metricsQuery.data.map((metric: HealthMetric) => (
                    <tr key={metric.id} className="border-t border-border/60">
                      <td className="px-3 py-2">{metric.cameraId}</td>
                      <td className="px-3 py-2">{metric.online ? 'Sim' : 'Não'}</td>
                      <td className="px-3 py-2 text-muted-foreground">{metric.measuredAt ? new Date(metric.measuredAt).toLocaleString() : '-'}</td>
                      <td className="px-3 py-2">
                        <div className="flex justify-end gap-2">
                          <Button variant="ghost" size="sm" onClick={() => {
                            setEditingMetricId(String(metric.id));
                            setMetricForm({
                              tenantId: metric.tenantId,
                              cameraId: metric.cameraId,
                              online: metric.online,
                              fps: metric.fps,
                              bitrate: metric.bitrate,
                              recordingConfidence: metric.recordingConfidence,
                              measuredAt: toLocalValue(metric.measuredAt),
                            });
                          }}>
                            <Pencil className="h-4 w-4" />
                          </Button>
                          <Button variant="ghost" size="sm" onClick={() => deleteMetricMutation.mutate(String(metric.id))}>
                            <Trash2 className="h-4 w-4" />
                          </Button>
                        </div>
                      </td>
                    </tr>
                  )) : (
                    <tr><td className="px-3 py-6 text-center text-muted-foreground" colSpan={4}>Nenhuma métrica registrada.</td></tr>
                  )}
                </tbody>
              </table>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2 text-xl"><Users className="h-5 w-5" /> User Sessions</CardTitle>
            <CardDescription>Gerencie sessões ativas e tokens emitidos.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <form onSubmit={submitSession} className="grid gap-4 rounded-xl border border-border/60 p-4">
              <div className="grid gap-4 sm:grid-cols-2">
                <div className="grid gap-2">
                  <Label htmlFor="session-user">User ID</Label>
                  <Input id="session-user" value={sessionForm.userId} onChange={(event) => setSessionForm((prev) => ({ ...prev, userId: event.target.value }))} />
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="session-token">Token ID</Label>
                  <Input id="session-token" value={sessionForm.tokenId} onChange={(event) => setSessionForm((prev) => ({ ...prev, tokenId: event.target.value }))} />
                </div>
              </div>
              <div className="grid gap-4 sm:grid-cols-2">
                <div className="grid gap-2">
                  <Label htmlFor="session-expires">Expira em</Label>
                  <Input id="session-expires" type="datetime-local" value={sessionForm.expiresAt || ''} onChange={(event) => setSessionForm((prev) => ({ ...prev, expiresAt: event.target.value }))} />
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="session-activity">Última atividade</Label>
                  <Input id="session-activity" type="datetime-local" value={sessionForm.lastActivityAt || ''} onChange={(event) => setSessionForm((prev) => ({ ...prev, lastActivityAt: event.target.value }))} />
                </div>
              </div>
              <div className="flex items-center justify-end gap-2">
                {editingSessionId && (
                  <Button type="button" variant="outline" onClick={() => { setEditingSessionId(null); setSessionForm({ tenantId: currentTenantId || '', userId: '', tokenId: '', expiresAt: nowToLocalValue(), lastActivityAt: nowToLocalValue() }); }}>
                    Cancelar
                  </Button>
                )}
                <Button type="submit" disabled={saveSessionMutation.isPending}>
                  {editingSessionId ? <Save className="mr-2 h-4 w-4" /> : <Plus className="mr-2 h-4 w-4" />}
                  {editingSessionId ? 'Atualizar sessão' : 'Criar sessão'}
                </Button>
              </div>
            </form>

            <div className="overflow-x-auto rounded-xl border border-border/60">
              <table className="w-full text-sm">
                <thead className="bg-muted/40 text-left text-muted-foreground">
                  <tr>
                    <th className="px-3 py-2">User</th>
                    <th className="px-3 py-2">Token</th>
                    <th className="px-3 py-2">Expira</th>
                    <th className="px-3 py-2 text-right">Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {sessionsQuery.data?.length ? sessionsQuery.data.map((session: UserSession) => (
                    <tr key={session.id} className="border-t border-border/60">
                      <td className="px-3 py-2">{session.userId}</td>
                      <td className="px-3 py-2 text-muted-foreground">{session.tokenId}</td>
                      <td className="px-3 py-2 text-muted-foreground">{session.expiresAt ? new Date(session.expiresAt).toLocaleString() : '-'}</td>
                      <td className="px-3 py-2">
                        <div className="flex justify-end gap-2">
                          <Button variant="ghost" size="sm" onClick={() => {
                            setEditingSessionId(String(session.id));
                            setSessionForm({
                              tenantId: session.tenantId,
                              userId: session.userId,
                              tokenId: session.tokenId,
                              expiresAt: toLocalValue(session.expiresAt),
                              lastActivityAt: toLocalValue(session.lastActivityAt),
                            });
                          }}>
                            <Pencil className="h-4 w-4" />
                          </Button>
                          <Button variant="ghost" size="sm" onClick={() => deleteSessionMutation.mutate(String(session.id))}>
                            <Trash2 className="h-4 w-4" />
                          </Button>
                        </div>
                      </td>
                    </tr>
                  )) : (
                    <tr><td className="px-3 py-6 text-center text-muted-foreground" colSpan={4}>Nenhuma sessão ativa.</td></tr>
                  )}
                </tbody>
              </table>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
