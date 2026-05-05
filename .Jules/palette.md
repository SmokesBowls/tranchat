## 2024-05-15 - Interactive Overlay UI Patterns
**Learning:** Found that icon buttons overlapping dark/video placeholders in `Room.jsx` needed distinct focus ring colors (`focus-visible:ring-white`) compared to those on light backgrounds like `SetPassword` and `JoinRoom` (`focus-visible:ring-blue-500`) to ensure focus contrast ratio is maintained.
**Action:** When adding focus states for elements positioned over generic dark backgrounds or camera feeds, explicitly override the standard `blue-500` focus ring with a high-contrast alternative like `white` or `gray-200`.
