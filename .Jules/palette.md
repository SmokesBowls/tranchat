
## 2023-10-27 - Icon-Only Button Accessibility Pattern
**Learning:** Found a recurring pattern of icon-only back/exit buttons across React components (`JoinRoom`, `SetPassword`, `Room`) lacking screen reader labels and keyboard focus indicators. The standard focus rings (`focus-visible:ring-blue-500`) are difficult to see on dark overlays (like in `Room.jsx`), requiring contrast-specific colors (`focus-visible:ring-white`).
**Action:** Applied a reusable pattern: add `aria-label` to the `<button>`, `aria-hidden="true"` to the inner `<svg>`, and context-aware `focus-visible:ring-2` styling. This pattern should be consistently applied to all new interactive icons in the codebase.
