## 2024-06-13 - Icon-only Button Accessibility
**Learning:** The application extensively uses icon-only buttons (like Back and Exit buttons) without `aria-label`, `aria-hidden` on the SVG, and focus management. Furthermore, buttons on dark overlays require `focus-visible:ring-white` instead of the default `ring-blue-500`.
**Action:** Always add `aria-label` to icon-only buttons, set `aria-hidden="true"` on the inner SVG, and use `focus-visible:outline-none focus-visible:ring-2` with an appropriate ring color based on the background contrast.
