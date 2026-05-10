import React from 'react';

export function Form({ children }: { children: React.ReactNode }) {
  return <>{children}</>;
}

export function FormControl({ children }: { children: React.ReactNode }) {
  return <>{children}</>;
}

export function FormField({ render }: { render: (props: any) => React.ReactNode; [key: string]: any }) {
  return <>{render({ field: {} })}</>;
}

export function FormItem({ children }: { children: React.ReactNode }) {
  return <div className="space-y-2">{children}</div>;
}

export function FormLabel({ children }: { children: React.ReactNode }) {
  return <label className="text-sm font-medium">{children}</label>;
}

export function FormMessage({ children }: { children?: React.ReactNode }) {
  return children ? <p className="text-sm text-destructive">{children}</p> : null;
}

export default Form;
