## 2024-04-14 - Icon-only Navigation Buttons Missing Context
**Learning:** The application uses several icon-only navigation buttons (e.g., Back, Exit) without explicit `aria-label`, `title`, or keyboard focus styles, which poses a severe accessibility issue for screen readers and keyboard users.
**Action:** Always add `aria-label`, `title`, `aria-hidden="true"` on decorative inner SVGs, and explicit keyboard focus visibility classes (e.g., `focus-visible:ring-2 focus:outline-none`) to interactive elements.
