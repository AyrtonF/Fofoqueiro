'use client';

import { Card, CardContent, CardHeader, CardTitle, CardDescription, CardFooter } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import Link from 'next/link';
import { Camera, Monitor, Clock, ShieldAlert, TriangleAlert, Activity } from 'lucide-react';
import { useQuery } from '@tanstack/react-query';
import { cameraService } from '@/services/camera-service';
import { alertService } from '@/services/alert-service';

export default function DashboardPage() {
  const { data: cameras } = useQuery({
    queryKey: ['dashboard-cameras'],
    queryFn: cameraService.getAll,
  });

  const { data: cameraHealth } = useQuery({
    queryKey: ['dashboard-camera-health'],
    queryFn: cameraService.getHealth,
  });

  const { data: alertEvents } = useQuery({
    queryKey: ['dashboard-alert-events'],
    queryFn: alertService.getAlertEvents,
  });

  const totalCameras = cameras?.length ?? 0;
  const onlineCameras = cameraHealth?.filter((item) => item.online).length ?? 0;
  const offlineCameras = Math.max(totalCameras - onlineCameras, 0);
  const activeAlerts = alertEvents?.filter((event) => !event.acknowledged).length ?? 0;

  const dashboardSections = [
    {
      title: 'Monitoramento Ao Vivo',
      description: 'Visualize o status em tempo real de todas as suas câmeras.',
      link: '/monitoring',
      icon: Camera,
      bgClass: 'from-blue-500 to-blue-700',
    },
    {
      title: 'Gerenciamento de Câmeras',
      description: 'Adicione, edite ou remova câmeras do seu sistema.',
      link: '/cameras',
      icon: Monitor,
      bgClass: 'from-green-500 to-green-700',
    },
    {
      title: 'Reprodução de Gravações',
      description: 'Acesse e revise gravações passadas de qualquer câmera.',
      link: '/playback/some-camera-id',
      icon: Clock,
      bgClass: 'from-yellow-500 to-yellow-700',
    },
    {
      title: 'Relatório de Auditoria',
      description: 'Revise logs de acesso e atividades do sistema.',
      link: '/audit-report',
      icon: ShieldAlert,
      bgClass: 'from-red-500 to-red-700',
    },
  ];

  return (
    <div className="w-full p-6 space-y-8">
      <div>
        <h1 className="text-4xl font-bold mb-3">Bem-vindo ao Fofoqueiro</h1>
        <p className="text-lg text-muted-foreground">
          Seu painel central para monitoramento, gerenciamento e segurança.
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-4">
        <Card>
          <CardHeader className="pb-2">
            <CardDescription>Câmeras cadastradas</CardDescription>
            <CardTitle className="text-3xl">{totalCameras}</CardTitle>
          </CardHeader>
          <CardContent className="text-sm text-muted-foreground flex items-center gap-2">
            <Monitor className="h-4 w-4" /> Inventário principal de dispositivos
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardDescription>Câmeras online</CardDescription>
            <CardTitle className="text-3xl">{onlineCameras}</CardTitle>
          </CardHeader>
          <CardContent className="text-sm text-muted-foreground flex items-center gap-2">
            <Activity className="h-4 w-4" /> Integridade calculada por saúde
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardDescription>Câmeras offline</CardDescription>
            <CardTitle className="text-3xl">{offlineCameras}</CardTitle>
          </CardHeader>
          <CardContent className="text-sm text-muted-foreground flex items-center gap-2">
            <TriangleAlert className="h-4 w-4" /> Acompanhe a operação em tempo real
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardDescription>Alertas pendentes</CardDescription>
            <CardTitle className="text-3xl">{activeAlerts}</CardTitle>
          </CardHeader>
          <CardContent className="text-sm text-muted-foreground flex items-center gap-2">
            <ShieldAlert className="h-4 w-4" /> Eventos sem acknowledge
          </CardContent>
        </Card>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {dashboardSections.map((section) => (
            <Card key={section.link} className={`relative overflow-hidden`}>
              <div className={`absolute inset-0 opacity-30 ${section.bgClass}`}></div>
              <CardHeader className="relative z-10">
                <section.icon className="h-12 w-12 text-primary-foreground mb-4" />
                <CardTitle className="text-xl text-primary-foreground">{section.title}</CardTitle>
                <CardDescription className="text-secondary-foreground/80">{section.description}</CardDescription>
              </CardHeader>
              <CardContent className="relative z-10 mt-4">
                {/* Placeholder for quick stats or actions */}
              </CardContent>
              <CardFooter className="relative z-10">
                <Link href={section.link} passHref>
                  <Button variant="secondary" className="w-full">
                    Acessar {section.title.split(' ')[0]}
                  </Button>
                </Link>
              </CardFooter>
            </Card>
          ))}
      </div>

      <div className="grid grid-cols-1 xl:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <CardTitle>Alertas recentes</CardTitle>
            <CardDescription>Eventos operacionais mais recentes do backend.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            {(alertEvents ?? []).slice(0, 5).map((event) => (
              <div key={event.id} className="rounded-md border p-3 flex items-start justify-between gap-4">
                <div>
                  <p className="font-medium">{event.eventType}</p>
                  <p className="text-sm text-muted-foreground">Camera {event.cameraId} - {event.description || 'Sem descrição'}</p>
                </div>
                <span className={`text-xs rounded-full px-2 py-1 ${event.acknowledged ? 'bg-emerald-500/10 text-emerald-700' : 'bg-amber-500/10 text-amber-700'}`}>
                  {event.acknowledged ? 'Reconhecido' : 'Pendente'}
                </span>
              </div>
            ))}
            {!alertEvents?.length && (
              <p className="text-sm text-muted-foreground">Nenhum alerta recente encontrado.</p>
            )}
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Saúde das câmeras</CardTitle>
            <CardDescription>Resumo de disponibilidade e sinais do backend.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            {(cameraHealth ?? []).slice(0, 5).map((item) => (
              <div key={item.id} className="flex items-center justify-between rounded-md border p-3">
                <div>
                  <p className="font-medium">Câmera {item.id}</p>
                  <p className="text-sm text-muted-foreground">{item.status || 'SEM_STATUS'}</p>
                </div>
                <span className={`text-xs rounded-full px-2 py-1 ${item.online ? 'bg-emerald-500/10 text-emerald-700' : 'bg-rose-500/10 text-rose-700'}`}>
                  {item.online ? 'Online' : 'Offline'}
                </span>
              </div>
            ))}
            {!cameraHealth?.length && (
              <p className="text-sm text-muted-foreground">Nenhuma métrica de saúde disponível.</p>
            )}
          </CardContent>
        </Card>
      </div>
      </div>
    );
}
