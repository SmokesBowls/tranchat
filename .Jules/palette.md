## 2024-06-27 - Icon-only Buttons Missing Accessibility

**Learning:** When using icon-only buttons (like the `Go back` or `Leave room` arrows), screen readers might announce nothing or the raw SVG elements if `aria-label` is missing. Additionally, these elements often lack focus outlines on keyboard navigation, which is an issue especially on different background contrasts.
**Action:** Always add an `aria-label` to the parent `<button>`, add `aria-hidden="true"` to the inner `<svg>` to hide it from screen readers, and implement visible focus indicators using Tailwind classes (e.g. `focus-visible:ring-2`, specifying a color that contrasts with the background, like `focus-visible:ring-white` for dark overlays).
