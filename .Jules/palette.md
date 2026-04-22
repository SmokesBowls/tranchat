## 2024-04-22 - Missing `<label>` for message input
**Learning:** The chat message input in `Room.jsx` lacked an explicit `<label>`, which can be detrimental to screen reader users attempting to understand what the input is for. In situations where a visible label would break the design, an `aria-label` attribute is a suitable fallback.
**Action:** Always ensure that `<input>` elements either have an associated `<label>` element or, when a visible label is not feasible, an `aria-label` attribute that clearly describes the input's purpose.
