import React from 'react';

export function AddCameraForm({ onAdd }: { onAdd?: (c: any) => void }) {
  return (
    <form className="space-y-2" onSubmit={e => { e.preventDefault(); onAdd && onAdd({ id: 'new', name: 'Nova Câmera' }); }}>
      <input name="name" className="input" placeholder="Nome da Câmera" />
      <button type="submit" className="btn">Adicionar</button>
    </form>
  );
}

export default AddCameraForm;
