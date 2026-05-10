import React from 'react';

export function CameraGrid({ cameras }: { cameras?: any[] }) {
  return (
    <div className="grid grid-cols-3 gap-4">
      {(cameras ?? []).map((c: any) => (
        <div key={c.id} className="p-2 border rounded">{c.name ?? 'Camera'}</div>
      ))}
    </div>
  );
}

export default CameraGrid;
