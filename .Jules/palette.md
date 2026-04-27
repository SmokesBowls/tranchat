
## 2024-04-27 - Icon-only buttons accessibility
**Learning:** Icon-only buttons without inner text must explicitly use `aria-label` along with `aria-hidden="true"` on the inner SVG. Otherwise, screen readers will miss them, and decorative SVGs will pollute the accessibility tree.
**Action:** When implementing icon-only controls, always attach an `aria-label` and `title` to the parent button, ensure keyboard visibility via `focus-visible:ring-*`, and hide the child SVG using `aria-hidden="true"`.
