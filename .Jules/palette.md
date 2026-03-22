
## 2024-05-15 - Focus visibility on dark overlays
**Learning:** Icon-only buttons placed over dark, translucent overlays (like video feeds or chat sidebars) require `focus-visible:ring-white` rather than the default `focus-visible:ring-blue-500` to maintain sufficient contrast and visibility for keyboard users.
**Action:** Always verify keyboard focus contrast against the immediate background layer, overriding standard design system colors (like blue rings) with white when placed on dark backgrounds.
