'use client';

import MainLayout from '@/app/MainLayout';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import Link from 'next/link';
import { Camera, Monitor, Clock, ShieldAlert } from 'lucide-react'; // Icons for modules

export default function DashboardPage() {
  const dashboardSections = [
    {
      title: 'Monitoramento Ao Vivo',
      description: 'Visualize o status em tempo real de todas as suas câmeras.',
      link: '/monitoring',
      icon: Camera,
      bgClass: 'from-blue-500 to-blue-700', // Example gradient
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
      link: '/playback/some-camera-id', // Link to a placeholder camera ID, ideally dynamic
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
    <MainLayout>
      <div className="w-full p-6">
        <h1 className="text-4xl font-bold mb-8">Bem-vindo ao Fofoqueiro</h1>
        <p className="text-lg text-muted-foreground mb-6">
          Seu painel central para monitoramento, gerenciamento e segurança.
        </p>

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
      </div>
    </MainLayout>
  );
}
