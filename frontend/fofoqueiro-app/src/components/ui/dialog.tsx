"use client";

import React, { cloneElement, createContext, createElement, useContext, useEffect, useId, useMemo, useState } from 'react';
import { createPortal } from 'react-dom';
import { X } from 'lucide-react';
import { cn } from '@/lib/utils';

type DialogContextValue = {
	open: boolean;
	setOpen: (open: boolean) => void;
	contentId: string;
};

const DialogContext = createContext<DialogContextValue | null>(null);

type DialogProps = {
	children: React.ReactNode;
	open?: boolean;
	onOpenChange?: (open: boolean) => void;
};

type DialogTriggerProps = {
	children: React.ReactElement;
	asChild?: boolean;
};

type DialogContentProps = React.HTMLAttributes<HTMLDivElement> & {
	children: React.ReactNode;
};

export const Dialog = ({ children, open, onOpenChange }: DialogProps) => {
	const [internalOpen, setInternalOpen] = useState(false);
	const contentId = useId();
	const isControlled = typeof open === 'boolean';
	const resolvedOpen = isControlled ? open : internalOpen;

	const setOpen = (nextOpen: boolean) => {
		if (!isControlled) {
			setInternalOpen(nextOpen);
		}
		onOpenChange?.(nextOpen);
	};

	const value = useMemo(() => ({ open: resolvedOpen, setOpen, contentId }), [resolvedOpen, contentId]);

	useEffect(() => {
		if (!resolvedOpen || typeof document === 'undefined') {
			return;
		}

		const previousOverflow = document.body.style.overflow;
		document.body.style.overflow = 'hidden';

		return () => {
			document.body.style.overflow = previousOverflow;
		};
	}, [resolvedOpen]);

	return <DialogContext.Provider value={value}>{children}</DialogContext.Provider>;
};

export const DialogTrigger = ({ children, asChild = false }: DialogTriggerProps) => {
	const context = useContext(DialogContext);

	if (!context) {
		return children;
	}

	const handleClick = () => context.setOpen(true);

	if (asChild && React.isValidElement(children)) {
		const child = children as React.ReactElement<any>;

		return cloneElement(child, {
			onClick: (event: React.MouseEvent) => {
				child.props?.onClick?.(event);
				if (!event.defaultPrevented) {
					handleClick();
				}
			},
			'aria-haspopup': 'dialog',
			'aria-expanded': context.open,
			'aria-controls': context.contentId,
		} as any);
	}

	return createElement(
		'button',
		{
			type: 'button',
			onClick: handleClick,
			'aria-haspopup': 'dialog',
			'aria-expanded': context.open,
			'aria-controls': context.contentId,
		},
		children
	);
};

export const DialogContent = ({ children, className, ...props }: DialogContentProps) => {
	const context = useContext(DialogContext);

	if (!context?.open) {
		return null;
	}

	const dialogNode = (
		<div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/70 p-4" onClick={() => context.setOpen(false)}>
			<div
				className={cn('relative w-full max-w-lg rounded-xl border border-border bg-card p-6 text-card-foreground shadow-2xl', className)}
				id={context.contentId}
				role="dialog"
				aria-modal="true"
				onClick={(event) => event.stopPropagation()}
				{...props}
			>
				<button
					type="button"
					aria-label="Fechar"
					className="absolute right-4 top-4 inline-flex h-8 w-8 items-center justify-center rounded-md text-muted-foreground transition-colors hover:bg-accent hover:text-accent-foreground"
					onClick={() => context.setOpen(false)}
				>
					<X className="h-4 w-4" />
				</button>
				{children}
			</div>
		</div>
	);

	if (typeof document === 'undefined') {
		return dialogNode;
	}

	return createPortal(dialogNode, document.body);
};

export const DialogHeader = ({ children }: any) => <div className="mb-4 space-y-1.5">{children}</div>;
export const DialogTitle = ({ children }: any) => <h3 className="text-lg font-semibold leading-none tracking-tight">{children}</h3>;
export const DialogDescription = ({ children }: any) => <p className="text-sm text-muted-foreground">{children}</p>;
export const DialogFooter = ({ children }: any) => <div className="mt-6 flex items-center justify-end gap-2">{children}</div>;

export default Dialog;
