## 2024-06-30 - Focus Rings on Dark Overlays
**Learning:** Default focus rings (`focus-visible:ring-blue-500` or the browser default) often disappear entirely when used on components placed over `bg-black bg-opacity-50` dark overlays.
**Action:** When adding keyboard accessibility to elements on dark transparent overlays, specifically use `focus-visible:ring-white` to ensure the focus ring remains visible and maintains adequate contrast.
