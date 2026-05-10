'use client';

import React from 'react';

export const Select: React.FC<React.PropsWithChildren<any>> = ({ children, className }) => {
  return <div className={className}>{children}</div>;
};

export const SelectTrigger: React.FC<React.PropsWithChildren<any>> = ({ children, className, ...props }) => {
  return (
    <select className={className} {...props}>
      {children}
    </select>
  );
};

export const SelectContent: React.FC<React.PropsWithChildren<any>> = ({ children, className }) => {
  return <div className={className}>{children}</div>;
};

export const SelectItem: React.FC<React.PropsWithChildren<any>> = ({ value, children }) => {
  return <option value={value}>{children}</option>;
};

export const SelectValue: React.FC<React.PropsWithChildren<any>> = ({ children }) => {
  return <span>{children}</span>;
};
