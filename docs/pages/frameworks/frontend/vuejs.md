# Vue.js

---

## Table of Contents
<!-- TOC -->
* [Vue.js](#vuejs)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [A Progressive Framework](#a-progressive-framework)
  * [Template Syntax vs. JSX](#template-syntax-vs-jsx)
  * [The Reactivity System](#the-reactivity-system)
  * [React vs. Angular vs. Vue.js](#react-vs-angular-vs-vuejs)
  * [Key Concepts](#key-concepts)
  * [Examples](#examples)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

**Vue.js** is an open-source frontend framework created by Evan You in 2014, explicitly positioned between [React](react.md)'s minimal library and [Angular](angular.md)'s all-in framework. Its defining pitch is being **progressive**: a team can drop Vue into a single page of an existing server-rendered app, or scale it up into a full single-page application with routing and state management, without committing to a rewrite either way.

---

## Overview

Vue was built partly as a reaction to both React and Angular: Evan You wanted Angular's approachable, HTML-based templates without its full framework weight, and React's reactive rendering model without requiring JSX or a build step to get started. The result is a library that can be included via a single `<script>` tag for small enhancements, or scaled into a full application with Vue Router, Pinia (state management), and a CLI/Vite-based build pipeline — the team decides how much of the ecosystem to adopt, and when.

At its core, Vue's mental model is its **reactivity system**: plain JavaScript objects become reactive proxies, and the framework automatically tracks which parts of the UI depend on which pieces of state, re-rendering only what changed when that state is mutated. This is a different mechanism from React's virtual DOM diffing, though both aim at the same goal — keeping the UI in sync with state efficiently.

<sub>[Back to top](#table-of-contents)</sub>

---

## A Progressive Framework

- ### Incremental Adoption:
  Vue can be introduced into an existing project one component at a time — for example, adding interactivity to a single widget on an otherwise server-rendered page — without adopting a build toolchain, a router, or a global state store. This is explicitly unlike [Angular](angular.md), which expects a project to be structured its way (CLI-scaffolded, TypeScript, its own router and forms module) from the start.

<sub>[Back to top](#table-of-contents)</sub>

- ### Scaling Up When Needed:
  When an application grows, the same Vue core scales up with official, first-party libraries — Vue Router for routing, Pinia for state management, Vite for tooling — that are maintained by the Vue team but remain optional add-ons rather than mandatory framework parts. This gives Vue some of Angular's "official answer for everything" convenience while preserving React-like opt-in flexibility.

<sub>[Back to top](#table-of-contents)</sub>

---

## Template Syntax vs. JSX

- ### HTML-Based Templates:
  Vue components typically use an HTML-based template syntax with directives (`v-if`, `v-for`, `v-bind`, `v-on`) rather than JSX. This keeps markup closer to plain HTML, which many teams (and designers) find more approachable, and lets Vue's compiler perform template-specific optimizations (like hoisting static content) that are harder to infer from arbitrary JSX.

  ```html
  <template>
    <ul>
      <li v-for="item in items" :key="item.id" @click="toggle(item)">
        {{ item.text }}
      </li>
    </ul>
  </template>
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### Compared to React's JSX:
  [React](react.md) embeds markup directly inside JavaScript via JSX, giving the full power of the language (arbitrary expressions, functions) at the cost of blurring the line between logic and markup. Vue's templates are a constrained, HTML-like DSL — less flexible, but easier to statically analyze and often easier to read for UI-focused work. Vue also supports JSX as an opt-in alternative for teams that prefer it.

<sub>[Back to top](#table-of-contents)</sub>

---

## The Reactivity System

- ### Reactive Proxies:
  Vue wraps plain JavaScript objects in reactive proxies (via `ref()` and `reactive()` in the Composition API). Reading a reactive value inside a component's render function registers a dependency; writing to it later automatically triggers only the components that depend on it to re-render.

  ```javascript
  import { ref, computed } from 'vue';

  const count = ref(0);
  const doubled = computed(() => count.value * 2);

  function increment() {
    count.value++; // triggers re-render of anything reading count or doubled
  }
  ```

  ```mermaid
  flowchart LR
      State[Reactive State: count] -->|tracked by| Effect1[Template render]
      State -->|tracked by| Effect2[computed: doubled]
      Mutation[count.value++] -->|triggers| State
      State -->|notifies| Effect1
      State -->|notifies| Effect2
  ```

  **Caption:** Vue's reactivity system tracks fine-grained dependencies between state and the effects (renders, computed values) that read it.

<sub>[Back to top](#table-of-contents)</sub>

- ### Fine-Grained vs. Virtual DOM Diffing:
  Because Vue tracks exactly which state each piece of the UI depends on, it can update precisely the affected DOM nodes without diffing an entire component subtree, which is conceptually different from React's approach of re-rendering a component function and diffing the resulting virtual DOM tree. In practice both achieve efficient updates, but Vue's model is dependency-tracking-based rather than diff-based.

<sub>[Back to top](#table-of-contents)</sub>

---

## React vs. Angular vs. Vue.js

- ### A Three-Way Comparison:
  Placing all three side by side clarifies where each sits on the spectrum from library to framework.

  | Aspect | [React](react.md) | [Angular](angular.md) | Vue.js |
  |--------|-------|---------|--------|
  | Category | Rendering library | Full framework | Progressive framework |
  | Language | JavaScript (TS optional) | TypeScript-first | JavaScript (TS supported) |
  | Markup | JSX | HTML templates | HTML templates (JSX optional) |
  | State update model | Virtual DOM diffing | Change detection / signals | Fine-grained reactivity |
  | Routing, state mgmt | Third-party, team's choice | Official, built-in | Official, opt-in |
  | Adoption style | All-in from the start | All-in from the start | Incremental or all-in |

<sub>[Back to top](#table-of-contents)</sub>

---

## Key Concepts

| Term | Definition |
|------|------------|
| Progressive framework | A framework adoptable incrementally, from a single widget up to a full SPA |
| Reactivity system | Vue's mechanism for tracking dependencies between reactive state and the UI that reads it |
| `ref()` / `reactive()` | Composition API functions that wrap values/objects in reactive proxies |
| Composition API | Function-based API (`setup()`, `ref`, `computed`) introduced in Vue 3, comparable to React Hooks |
| Single-File Component (SFC) | A `.vue` file combining template, script, and scoped styles in one file |
| Directive | Template attribute (`v-if`, `v-for`, `v-bind`) that adds reactive behavior to HTML |

<sub>[Back to top](#table-of-contents)</sub>

---

## Examples

A single-file component using the Composition API, combining a template, reactive state, and scoped styling:

```vue
<template>
  <button @click="increment">Clicked {{ count }} times</button>
</template>

<script setup>
import { ref } from 'vue';
const count = ref(0);
function increment() {
  count.value++;
}
</script>

<style scoped>
button { padding: 0.5rem 1rem; }
</style>
```

The `<script setup>` block declares reactive state with `ref()`; the template reads and updates it directly, and Vue's reactivity system handles re-rendering the button's text automatically when `count` changes.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: When would I choose Vue over React or Angular for a new project?**
A: Choose Vue when you want Angular-like official answers for routing and state management but don't want to commit fully upfront, or when incremental adoption into an existing (possibly server-rendered) app matters — e.g., modernizing one page at a time. Choose React for maximum ecosystem flexibility and a large talent pool; choose Angular when enforced consistency across many teams outweighs flexibility.

---

**Q: How does Vue's reactivity system differ from React's virtual DOM?**
A: Vue tracks which specific pieces of state each part of the UI reads, so a state change notifies only the affected consumers directly. React instead re-runs the component function on state change and diffs the resulting virtual DOM tree against the previous one to find what changed. Both end up updating only the necessary real DOM nodes, but Vue's approach is dependency-tracking-based while React's is diff-based.

---

**Q: Is Vue's "progressive" positioning just marketing, or does it have real architectural implications?**
A: It's real: a team can add Vue to a single legacy page without a build step (via a CDN script tag) and later migrate to a full Vite + Vue Router + Pinia SPA using the same component model throughout. Neither React (which expects JSX and typically a bundler) nor Angular (which expects its CLI-driven project structure from day one) offers that same low-commitment entry point.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [React](react.md) — the minimal rendering library Vue borrows its component and reactive-update goals from
- [Angular](angular.md) — the full framework Vue borrows official routing/state tooling conventions from, without the mandatory adoption
- [Frontend Styling & Tooling](frontend-styling-tooling.md) — CSS preprocessors and component libraries commonly paired with Vue's scoped SFC styles

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Vue.js Documentation](https://vuejs.org/) — official documentation and guide
- [Vue.js: Reactivity in Depth](https://vuejs.org/guide/extras/reactivity-in-depth.html) — official explanation of the reactivity system

---

[Get Started](../../../get-started.md) | [Web Development Frameworks](../../../get-started.md#web-development-frameworks)

---
