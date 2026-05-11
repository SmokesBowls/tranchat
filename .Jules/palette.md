
## $(date +%Y-%m-%d) - Focus Rings on Dark Overlays
**Learning:** Default Tailwind focus rings (`ring-blue-500`) are practically invisible when applied to interactive elements rendered over dark backgrounds (e.g., `bg-black bg-opacity-50`).
**Action:** When making components on dark overlays keyboard-accessible, explicitly use a high-contrast ring color like `focus-visible:ring-white` to ensure the focus state is clearly visible.
