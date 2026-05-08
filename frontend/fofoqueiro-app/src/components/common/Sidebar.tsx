import { NavigationItem, navigationItems } from '@/app/navigation'; // Assuming navigationItems is exported from navigation.ts
import Link from 'next/link';
import { Button } from '@/components/ui/button';
import { LogOut } from 'lucide-react';
import { cn } from '@/lib/utils';
import { useAuthStore } from '@/store/auth-store';
import { usePathname } from 'next/navigation'; // Hook to get current path

export function Sidebar() {
  const { logout } = useAuthStore();
  const pathname = usePathname(); // Get current path

  return (
    <aside className="fixed left-0 top-0 z-40 h-full w-64 bg-card border-r px-4 py-6 flex flex-col">
      <div className="flex items-center justify-center mb-8">
        <span className="text-xl font-bold text-primary">Fofoqueiro</span>
      </div>
      <nav className="flex-1 flex flex-col gap-2">
        {navigationItems.map((item: NavigationItem) => (
          <Link
            key={item.href}
            href={item.href}
            className={cn(
              "flex items-center gap-3 px-3 py-2 rounded-md text-muted-foreground hover:bg-accent hover:text-accent-foreground transition-colors",
              // Highlight active link
              pathname === item.href ? "bg-primary/10 text-primary" : ""
            )}
          >
            <item.icon className="h-5 w-5" />
            <span className="text-sm font-medium">{item.label}</span>
          </Link>
        ))}
      </nav>
      <div className="mt-auto">
        <Button variant="outline" className="w-full" onClick={() => logout()}>
          <LogOut className="h-4 w-4 mr-2" />
          Sair
        </Button>
      </div>
    </aside>
  );
}
