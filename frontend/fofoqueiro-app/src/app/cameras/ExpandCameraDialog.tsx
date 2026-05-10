import React from 'react';

export function ExpandCameraDialog({ camera, onClose }: { camera: any; onClose?: () => void }) {
  return (
    <div className="p-2 border rounded">
      <div>Expand {camera?.name ?? 'Camera'}</div>
      <button className="btn" onClick={() => onClose && onClose()}>Fechar</button>
    </div>
  );
}

export default ExpandCameraDialog;
