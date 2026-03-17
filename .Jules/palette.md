## 2024-03-17 - Added Accessibility to Icon-only Buttons
**Learning:** Found that icon-only buttons lacked aria-labels, titles, and focus-visible states across the app. Need to ensure decorative SVGs also have aria-hidden="true".
**Action:** Consistently added `aria-label`, `title`, explicit keyboard focus states (`focus-visible:ring-2`), and `aria-hidden="true"` to icon SVGs to improve keyboard navigation and screen reader support.
