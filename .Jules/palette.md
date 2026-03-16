## 2023-11-20 - Icon-Only Button Accessibility Pattern
**Learning:** Icon-only buttons in this app often lack `aria-label`, `title`, and keyboard focus styling (`focus-visible`). This makes them inaccessible to screen readers and difficult to use for keyboard-only users.
**Action:** When adding or reviewing icon-only buttons, always ensure they have an `aria-label` describing the action, a `title` for hover tooltips, keyboard focus rings (`focus-visible:ring-2 focus:outline-none`), and `aria-hidden="true"` on the inner SVG.
