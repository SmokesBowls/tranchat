## 2026-06-10 - Accessible Icon Buttons in Chat Flow
**Learning:** The application extensively uses icon-only back/exit buttons in modal-like overlays and forms without accessible names or focus indicators, causing confusion for screen reader and keyboard users.
**Action:** Added `aria-label` to the wrapper `<button>`, applied `aria-hidden="true"` to inner `<svg>` elements to prevent redundant announcements, and used Tailwind's `focus-visible` to add visible keyboard focus rings.
