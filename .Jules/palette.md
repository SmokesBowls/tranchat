## 2025-05-06 - Accessible Icon-Only Buttons
**Learning:** When adding ARIA labels to icon-only buttons (like back/exit SVG buttons), applying `aria-label` to the button wrapper is insufficient if the inner `<svg>` element isn't explicitly marked with `aria-hidden="true"`. Without this, screen readers may announce both the button label and attempt to parse the SVG content, causing redundant or confusing announcements.
**Action:** Always pair `aria-label` on the parent `<button>` with `aria-hidden="true"` on the child `<svg>` for icon-only controls.
