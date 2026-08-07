# React

---

## Table of Contents
<!-- TOC -->
* [React](#react)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Component-Based Architecture](#component-based-architecture)
  * [Virtual DOM and Reconciliation](#virtual-dom-and-reconciliation)
  * [Unidirectional Data Flow](#unidirectional-data-flow)
  * [A Library, Not a Framework](#a-library-not-a-framework)
  * [Key Concepts](#key-concepts)
  * [Examples](#examples)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

**React** is a JavaScript library, created and maintained by Meta, for building user interfaces out of composable, reusable components. It popularized the idea of describing UI declaratively as a function of state, and diffing that description against the previous one to compute the minimal set of real DOM updates. React is deliberately narrow in scope — it renders components and manages their re-rendering — which makes it architecturally distinct from batteries-included frameworks like Angular.

---

## Overview

React was released by Facebook in 2013 to solve a specific problem: keeping a complex, frequently-changing UI (the News Feed) in sync with underlying data without hand-written DOM manipulation becoming unmanageable. Its core idea — render UI as a pure function of state, then let the library figure out what changed — has since become the dominant mental model for frontend UI development, influencing Vue, SolidJS, and even native mobile frameworks.

Because React only concerns itself with rendering components and reacting to state changes, everything else an application needs — routing, global state management, data fetching, form handling — is left to the surrounding ecosystem (React Router, Redux/Zustand/Context, React Query, etc.). This makes React more of a UI rendering primitive than a complete application framework, a distinction with real architectural consequences discussed below.

<sub>[Back to top](#table-of-contents)</sub>

---

## Component-Based Architecture

A React application is a tree of components — small, self-contained units that each own their own markup, logic, and (optionally) local state. Components compose: a `Page` renders a `Header` and a `List`, which renders many `ListItem`s, and so on.

- ### Function Components and Hooks:
  Modern React expresses components as plain JavaScript functions that return JSX (a syntax extension that looks like HTML but compiles to `React.createElement` calls). **Hooks** (`useState`, `useEffect`, `useContext`, etc.) let function components hold local state and side effects without needing class syntax.

  ```jsx
  function Counter() {
    const [count, setCount] = useState(0);
    return (
      <button onClick={() => setCount(count + 1)}>
        Clicked {count} times
      </button>
    );
  }
  ```

  ```mermaid
  flowchart TD
      App --> Header
      App --> ListContainer
      ListContainer --> ListItem1[ListItem]
      ListContainer --> ListItem2[ListItem]
      ListContainer --> ListItem3[ListItem]
  ```

  **Caption:** A React UI is a composition tree — parent components pass data down to children via props.

<sub>[Back to top](#table-of-contents)</sub>

- ### Props and Composition:
  Data flows into a component through **props** (read-only inputs passed by the parent). Composition — building complex UI by nesting simpler components — is preferred over inheritance, mirroring the "favor composition over inheritance" principle from object-oriented design.

<sub>[Back to top](#table-of-contents)</sub>

---

## Virtual DOM and Reconciliation

- ### The Virtual DOM:
  On every state change, React builds a lightweight in-memory tree of JavaScript objects representing the desired UI — the **virtual DOM**. This is cheap to create and diff compared to touching the real, browser-native DOM, which is expensive to read and write.

<sub>[Back to top](#table-of-contents)</sub>

- ### Reconciliation:
  React compares (**diffs**) the new virtual DOM tree against the previous one using a heuristic algorithm, then computes the minimal set of real DOM mutations needed to bring the browser's DOM in sync. This process is called **reconciliation**. Keys (`key={id}`) on list items help React match elements across renders instead of re-creating them from scratch.

  ```mermaid
  sequenceDiagram
      participant State as State Change
      participant VDOM as Virtual DOM
      participant Diff as Reconciler
      participant DOM as Real DOM
      State->>VDOM: Re-render component tree
      VDOM->>Diff: Compare new tree vs. previous tree
      Diff->>DOM: Apply minimal patch (insert/update/remove)
  ```

  **Caption:** Reconciliation computes a minimal diff so only the changed DOM nodes are touched.

<sub>[Back to top](#table-of-contents)</sub>

---

## Unidirectional Data Flow

- ### One-Way Binding:
  Data in React flows in a single direction: from parent state down to child props. A child cannot directly mutate a prop it receives — if it needs to change shared state, the parent passes down a callback function the child invokes, and the parent decides how to update its own state in response.

  > This is the opposite of two-way data binding (see [Angular](angular.md)), where a UI control can write directly back into a bound model.

<sub>[Back to top](#table-of-contents)</sub>

- ### Why It Matters Architecturally:
  Unidirectional flow makes state changes traceable: given a bug, you can follow data from the single source of truth downward instead of chasing bidirectional update loops. This predictability is why most React state-management libraries (Redux, Zustand) also enforce one-way flow with explicit actions rather than direct mutation.

<sub>[Back to top](#table-of-contents)</sub>

---

## A Library, Not a Framework

- ### Rendering Library, Not an Application Framework:
  React only solves rendering: turning state into UI and keeping it updated. It ships no built-in router, no official global state solution, and no opinion on how to structure folders or fetch data. Everything else is a deliberate choice left to the team.

<sub>[Back to top](#table-of-contents)</sub>

- ### Architectural Consequence:
  This is significant for architects: adopting React means also making a series of separate ecosystem decisions — routing (React Router, TanStack Router), state management (Context, Redux, Zustand, Jotai), data fetching/caching (React Query, SWR), and meta-frameworks (Next.js, Remix) that bundle these choices into an opinionated stack. Two React codebases can look architecturally very different, whereas two Angular codebases converge on the same structure because the framework mandates it.

<sub>[Back to top](#table-of-contents)</sub>

---

## Key Concepts

| Term | Definition |
|------|------------|
| JSX | Syntax extension that lets component markup be written HTML-like inside JavaScript, compiled to function calls |
| Virtual DOM | In-memory representation of the UI used to compute minimal real-DOM updates |
| Reconciliation | The diffing algorithm that compares virtual DOM trees across renders |
| Hook | A function (`useState`, `useEffect`, ...) that lets function components use state and lifecycle features |
| Props | Read-only data passed from a parent component to a child |
| Unidirectional data flow | Data moves one way, parent to child; children request changes via callbacks |

<sub>[Back to top](#table-of-contents)</sub>

---

## Examples

A minimal component demonstrating props, local state, and unidirectional flow:

```jsx
function TodoItem({ text, done, onToggle }) {
  return (
    <li onClick={onToggle} style={{ textDecoration: done ? 'line-through' : 'none' }}>
      {text}
    </li>
  );
}

function TodoList({ items, onToggleItem }) {
  return (
    <ul>
      {items.map(item => (
        <TodoItem
          key={item.id}
          text={item.text}
          done={item.done}
          onToggle={() => onToggleItem(item.id)}
        />
      ))}
    </ul>
  );
}
```

`TodoList` owns the data and passes it down as props; `TodoItem` never mutates its own props — it calls `onToggle`, letting the parent decide how state changes. This is unidirectional data flow in practice.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: If React isn't a full framework, how do teams avoid ending up with inconsistent architectures across projects?**
A: By standardizing on a meta-framework (Next.js, Remix) or an internal "starter kit" that pins the router, state library, and folder conventions. Without that discipline, React's flexibility can indeed produce divergent codebases across teams — this is a real trade-off against Angular's enforced consistency.

---

**Q: Why does reconciliation need `key` props on list items?**
A: Without keys, React matches old and new elements by position, so inserting an item at the top of a list can cause every subsequent item to be misidentified and re-rendered (or worse, have its local state associated with the wrong element). A stable, unique `key` lets React match elements by identity across renders.

---

**Q: When would I choose React over Angular for a new project?**
A: Choose React when you want flexibility to pick best-of-breed tools per concern, have a team already fluent in the ecosystem, or are building something a meta-framework (Next.js) fits well, like an SSR marketing site or content-heavy app. Choose Angular when you want an enforced, consistent structure across many teams/services with minimal upfront tooling decisions — see [Angular](angular.md).

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Angular](angular.md) — the opinionated, full-framework alternative to React's library-only approach
- [Vue.js](vuejs.md) — a progressive framework that borrows React's component model but uses templates instead of JSX
- [Frontend Styling & Tooling](frontend-styling-tooling.md) — CSS preprocessors and component libraries commonly paired with React

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [React Documentation](https://react.dev/) — official documentation and learning guide
- [React: Reconciliation](https://legacy.reactjs.org/docs/reconciliation.html) — official explanation of the diffing algorithm

---

[Get Started](../../../get-started.md) | [Web Development Frameworks](../../../get-started.md#web-development-frameworks)

---
