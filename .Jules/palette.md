## 2026-04-17 - Missing Accessibility Attributes on Icon-Only Buttons
**Learning:** Found an accessibility issue pattern across this app's components: icon-only buttons (like back and exit buttons) frequently lack essential accessibility markers such as `aria-label`, `title`, keyboard focus indicators (e.g., `focus-visible:ring-2`), and `aria-hidden="true"` on their decorative inner SVGs.
**Action:** When working on UI components, routinely check and apply these accessibility markers to all icon-only buttons to ensure they are screen reader friendly and navigable via keyboard.
