import React from 'react';

export function Calendar(_props: any) {
  // Minimal placeholder for build; replace with real calendar later
  // Remove non-DOM props (like `initialFocus`) before spreading to a div
  const { initialFocus, ...props } = _props || {};
  return (
    <div {...props} className={`p-2 border rounded bg-white ${props.className ?? ''}`}>
      {/* simple placeholder calendar */}
      <div className="text-sm text-muted-foreground">Calendar</div>
    </div>
  );
}

export default Calendar;
