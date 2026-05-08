import { Gauge, Camera, Settings, LogOut, LayoutGrid, Monitor, ShieldAlert } from 'lucide-react'; // Added Monitor icon
import { NavigationItem } from './navigation';

export const navigationItems: NavigationItem[] = [
  { href: '/dashboard', label: 'Dashboard', icon: Gauge },
  { href: '/monitoring', label: 'Monitoramento', icon: Camera },
  { href: '/devices', label: 'Dispositivos', icon: Monitor }, // Added Devices link
  { href: '/cameras', label: 'Gerenciar Câmeras', icon: LayoutGrid },
  { href: '/audit-report', label: 'Auditoria', icon: ShieldAlert }, // Added Audit Report link
  { href: '/settings', label: 'Configurações', icon: Settings },
];
