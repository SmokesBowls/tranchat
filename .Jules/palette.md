## 2024-06-05 - Focus rings on dark overlays
**Learning:** Default blue focus rings (`focus-visible:ring-blue-500`) are difficult to see against dark semi-transparent backgrounds (like `bg-black bg-opacity-50`), creating an accessibility issue for keyboard users navigating video chat overlays.
**Action:** Use a white focus ring (`focus-visible:ring-white`) specifically for interactive elements positioned over dark overlays to ensure sufficient contrast and visibility for keyboard focus indicators.
