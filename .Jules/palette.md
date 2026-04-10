## 2024-04-10 - Icon-only Button Accessibility
**Learning:** Icon-only buttons often lack accessible names, tooltips, and explicit focus rings, rendering them unusable for screen reader users and difficult for keyboard navigators. Purely decorative SVGs inside these buttons need `aria-hidden="true"`.
**Action:** Always provide `aria-label` and `title` attributes on icon-only buttons, add explicit keyboard focus visibility classes (e.g., `focus-visible:ring-2 focus:outline-none`), and use `aria-hidden="true"` on their inner SVGs across all components.
