## 2024-05-18 - Icon-only Button Accessibility
**Learning:** Icon-only buttons (like back or close buttons) frequently lack ARIA labels and focus states. In this app, these buttons appear on both light backgrounds (using `focus-visible:ring-blue-500`) and dark overlays (using `focus-visible:ring-white`).
**Action:** Always add `aria-label`, `title`, and `focus-visible:ring-2 focus:outline-none` with the appropriate ring color to icon-only buttons, and apply `aria-hidden="true"` to their internal SVGs.
