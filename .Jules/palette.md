## 2024-05-24 - Accessible Icon Buttons in React
**Learning:** Found multiple icon-only buttons in `JoinRoom.jsx`, `SetPassword.jsx`, and `Room.jsx` lacking `aria-label` and `focus-visible` styling, hindering keyboard accessibility.
**Action:** Add `aria-label` and `aria-hidden="true"` to SVGs, and use `focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-500` (or `focus-visible:ring-white` on dark overlays).
