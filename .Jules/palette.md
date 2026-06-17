## 2024-06-17 - Keyboard Accessible Icon Buttons
**Learning:** Icon-only SVG buttons in this design system often lack screen reader context and default focus rings disappear against dark backgrounds (e.g. `bg-black bg-opacity-50` overlays).
**Action:** When creating icon-only buttons, always provide an `aria-label` on the `<button>`, set `aria-hidden="true"` on the inner `<svg>`, and explicitly define focus states. Use `focus-visible:ring-blue-500` for standard backgrounds and `focus-visible:ring-white` for dark overlays.
