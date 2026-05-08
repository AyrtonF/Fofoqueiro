import { Camera } from '@/domain/types';
import { useCameraMutation } from '@/hooks/use-camera-mutations';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form';
import { toast } from 'sonner';

const cameraSchema = z.object({
  name: z.string().min(3, "Mínimo 3 caracteres"),
  url: z.string().url("URL inválida"),
  gatewayId: z.string().min(1, "Obrigatório"),
  latitude: z.coerce.number().optional(),
  longitude: z.coerce.number().optional(),
});

export function AddCameraForm({ onSuccess }: { onSuccess: () => void }) {
  const { createCameraMutation, testConnectionMutation } = useCameraMutation();
  const form = useForm<z.infer<typeof cameraSchema>>({
    resolver: zodResolver(cameraSchema),
    defaultValues: { name: '', url: '', gatewayId: '', latitude: 0, longitude: 0 },
  });

  const onSubmit = async (values: z.infer<typeof cameraSchema>) => {
    const test = await testConnectionMutation.mutateAsync(values.url);
    if (!test.success) return toast.error(test.message);
    
    createCameraMutation.mutate(values, {
      onSuccess: () => {
        toast.success("Câmera criada!");
        onSuccess();
      }
    });
  };

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
        <FormField name="name" control={form.control} render={({ field }) => (
          <FormItem>
            <FormLabel>Nome</FormLabel>
            <FormControl><Input {...field} /></FormControl>
            <FormMessage />
          </FormItem>
        )} />
        <FormField name="url" control={form.control} render={({ field }) => (
          <FormItem>
            <FormLabel>URL RTSP</FormLabel>
            <FormControl><Input {...field} /></FormControl>
            <FormMessage />
          </FormItem>
        )} />
        <Button type="submit" disabled={createCameraMutation.isPending}>Salvar</Button>
      </form>
    </Form>
  );
}
