"use client";

import React, { cloneElement, createContext, createElement, useContext, useEffect, useMemo, useRef, useState } from 'react';
import { createPortal } from 'react-dom';
import { cn } from '@/lib/utils';

type PopoverContextValue = {
	open: boolean;
	setOpen: (open: boolean) => void;
	triggerRef: React.RefObject<HTMLElement | null>;
};

const PopoverContext = createContext<PopoverContextValue | null>(null);

type PopoverProps = {
	children: React.ReactNode;
	open?: boolean;
	onOpenChange?: (open: boolean) => void;
};

type PopoverTriggerProps = {
	children: React.ReactElement;
	asChild?: boolean;
};

type PopoverContentProps = React.HTMLAttributes<HTMLDivElement> & {
	children: React.ReactNode;
	align?: 'start' | 'center' | 'end';
	sideOffset?: number;
};

export const Popover = ({ children, open, onOpenChange }: PopoverProps) => {
	const [internalOpen, setInternalOpen] = useState(false);
	const triggerRef = useRef<HTMLElement | null>(null);
	const isControlled = typeof open === 'boolean';
	const resolvedOpen = isControlled ? open : internalOpen;

	const setOpen = (nextOpen: boolean) => {
		if (!isControlled) {
			setInternalOpen(nextOpen);
		}
		onOpenChange?.(nextOpen);
	};

	const value = useMemo(() => ({ open: resolvedOpen, setOpen, triggerRef }), [resolvedOpen]);

	useEffect(() => {
		if (!resolvedOpen) {
			return;
		}

		const handleEscape = (event: KeyboardEvent) => {
			if (event.key === 'Escape') {
				setOpen(false);
			}
		};

		document.addEventListener('keydown', handleEscape);
		return () => document.removeEventListener('keydown', handleEscape);
	}, [resolvedOpen]);

	return <PopoverContext.Provider value={value}>{children}</PopoverContext.Provider>;
};

export const PopoverTrigger = ({ children, asChild = false }: PopoverTriggerProps) => {
	const context = useContext(PopoverContext);

	if (!context) {
		return children;
	}

	const handleClick = () => context.setOpen(!context.open);

	if (asChild && React.isValidElement(children)) {
		const child = children as React.ReactElement<any>;

		return cloneElement(child, {
			ref: (node: HTMLElement | null) => {
				context.triggerRef.current = node;
			},
			onClick: (event: React.MouseEvent) => {
				child.props?.onClick?.(event);
				if (!event.defaultPrevented) {
					handleClick();
				}
			},
			'aria-haspopup': 'dialog',
			'aria-expanded': context.open,
		} as any);
	}

	return createElement(
		'button',
		{
			type: 'button',
			ref: context.triggerRef as any,
			onClick: handleClick,
			'aria-haspopup': 'dialog',
			'aria-expanded': context.open,
		},
		children
	);
};

export const PopoverContent = ({ children, className, align = 'center', sideOffset = 8, ...props }: PopoverContentProps) => {
	const context = useContext(PopoverContext);

	if (!context?.open) {
		return null;
	}

	const triggerRect = context.triggerRef.current?.getBoundingClientRect();
	const style = triggerRect
		? {
				position: 'fixed' as const,
				top: triggerRect.bottom + sideOffset,
				left: align === 'start' ? triggerRect.left : align === 'end' ? triggerRect.right : triggerRect.left + triggerRect.width / 2,
				transform: align === 'center' ? 'translateX(-50%)' : align === 'end' ? 'translateX(-100%)' : undefined,
				minWidth: triggerRect.width,
			}
		: undefined;

	const popoverNode = (
		<div className="fixed inset-0 z-50" onClick={() => context.setOpen(false)}>
			<div
				className={cn('absolute rounded-md border border-border bg-card p-3 text-card-foreground shadow-xl', className)}
				style={style}
				onClick={(event) => event.stopPropagation()}
				{...props}
			>
				{children}
			</div>
		</div>
	);

	if (typeof document === 'undefined') {
		return popoverNode;
	}

	return createPortal(popoverNode, document.body);
};

export default Popover;
