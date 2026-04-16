## 2024-04-16 - Context-Aware Focus Rings
**Learning:** Standard focus rings (`focus-visible:ring-blue-500`) are often invisible against dark overlays or modal backdrops used for video elements or dialogs.
**Action:** When adding keyboard focus visibility to interactive elements positioned over dark overlays (like the exit button in `Room.jsx`), use high-contrast focus rings (e.g., `focus-visible:ring-white`) instead of standard application colors to ensure the focus state remains visible to users navigating via keyboard.
