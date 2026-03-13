## 2024-05-24 - Accessible Icon-Only Buttons
**Learning:** Icon-only buttons frequently lack ARIA labels, title tooltips, inner SVG hidden states, and explicit visible focus rings across different contexts.
**Action:** When auditing or adding icon-only buttons, consistently apply `aria-label`, `title`, `aria-hidden="true"` on the SVG, and explicit focus-visible classes (`focus:outline-none focus-visible:ring-2`). Ensure focus ring colors contrast appropriately with their background context (e.g., `focus-visible:ring-white` for dark overlays).
