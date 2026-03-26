## 2025-02-15 - [Improve Accessibility of Icon-Only Buttons]
**Learning:** Icon-only buttons in absolute positioned dark overlays (like a video chat overlay) need high contrast focus rings, such as `focus-visible:ring-white`, for keyboard navigation visibility. Standard `ring-blue-500` may blend in.
**Action:** Always add explicit keyboard focus indicators with appropriate contrast relative to their background, along with `aria-label` and `title` attributes for screen readers, and `aria-hidden="true"` for inner SVGs to prevent redundant readout.
