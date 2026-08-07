# Quickstart: Verifying Navigation & Usability Overhaul

1. Open `docs/get-started.md` and search for the literal string `]()`. Expected: no matches (SC-001).
2. Open `docs/get-started.md` and search for `TODO`. Expected: no matches.
3. Confirm Distributed Transaction, Saga, Message-Driven, Event-Driven, Event Sourcing still appear as plain text (not removed, not linked).
4. Open `docs/index.md` and `docs/get-started.md` side by side. Confirm no paragraph is duplicated verbatim between them (SC-003).
5. Read the filter `<script>` block at the top of `get-started.md`; confirm it selects list items under the content area and toggles visibility on input, and confirm it fails safe (page still fully readable) if JS doesn't execute — the input/script sit above content that remains in the normal document flow.
