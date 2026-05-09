## 2024-05-09 - Accessible Icon Buttons
**Learning:** Icon-only buttons lacking `aria-label` and `focus-visible` styles are common across the app (e.g. back buttons, exit buttons).
**Action:** When adding or updating icon-only buttons, always ensure they have an `aria-label` on the parent `<button>`, `aria-hidden="true"` on the child SVG icon, and distinct `focus-visible` styles using Tailwind rings (e.g., `focus-visible:ring-blue-500` or `focus-visible:ring-white` for dark overlays).
