## 2024-03-24 - Accessibility for Icon-only Buttons
**Learning:** Found a recurring pattern in React components (`JoinRoom.jsx`, `SetPassword.jsx`, `Room.jsx`) where icon-only buttons lacked proper `aria-label`, `title` tooltips, keyboard focus visibility, and had screen-reader accessible decorative inner SVGs.
**Action:** Always verify that newly created or modified icon-only buttons include `aria-label`, `title`, explicit focus classes like `focus-visible:ring-2`, and `aria-hidden="true"` on purely decorative SVGs.
