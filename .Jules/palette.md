## 2024-06-08 - Icon-Only Button Accessibility and Keyboard Focus

**Learning:** When using React components like `JoinRoom`, `SetPassword`, and `Room` with floating or absolute positioned elements, default focus rings often disappear or have poor contrast. Dark overlay elements require explicitly contrasted focus rings (e.g., `ring-white`) instead of default or brand-colored ones. Furthermore, nested SVG icons inside buttons with `aria-label`s must have `aria-hidden="true"` explicitly set to prevent redundant or confusing screen reader announcements.

**Action:** Consistently apply explicit `focus-visible:ring-2` styles with appropriate high-contrast colors (e.g., `ring-white` for dark backgrounds, `ring-blue-500` for light) to interactive elements. Ensure all icon-only buttons have an `aria-label` and their child SVG icons have `aria-hidden="true"`.
