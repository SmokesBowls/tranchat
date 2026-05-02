## 2024-05-18 - Dark Overlay Focus Ring Visibility
**Learning:** Default Tailwind focus rings (`focus-visible:ring-blue-500`) disappear entirely when applied to icon buttons resting on dark overlays (`bg-black bg-opacity-50`), causing a critical failure in keyboard navigation accessibility.
**Action:** Always explicitly enforce a high-contrast focus ring (e.g., `focus-visible:ring-white`) for interactive elements layered over dark backgrounds or media feeds.
