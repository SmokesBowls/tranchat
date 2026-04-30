## 2024-04-30 - Accessible Icon-only Buttons
**Learning:** Icon-only buttons (like back/exit controls) in the app consistently used decorative SVGs without semantic meaning, `aria-label`s, `title` tooltips, or keyboard focus indicators. Furthermore, focus ring colors need to adapt to their background (e.g., `ring-white` on dark overlays vs standard `ring-blue-500`).
**Action:** When adding icon-only buttons, always include `aria-label`, `title`, explicit `focus-visible` styles matching the background contrast, and `aria-hidden="true"` on the SVG icon itself.
