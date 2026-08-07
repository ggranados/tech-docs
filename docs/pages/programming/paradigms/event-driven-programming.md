# Event-Driven Programming

---

## Table of Contents
<!-- TOC -->
* [Event-Driven Programming](#event-driven-programming)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Callbacks, Listeners, and the Event Loop](#callbacks-listeners-and-the-event-loop)
  * [Programming-Model vs. Architectural-Style](#programming-model-vs-architectural-style)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Event-driven programming is a paradigm where a program's control flow is determined by events — user actions, sensor outputs, messages from other programs — rather than by a fixed top-to-bottom sequence of instructions. Code registers interest in specific events and runs only when they occur.

---

## Overview

Instead of a program actively polling for work, an event-driven program sits idle until something happens (a click, a keypress, a socket receiving data, a timer firing), then runs the handler registered for that event. This inverts the usual "caller decides when code runs" model into "the event source decides when code runs" — often called the *Hollywood Principle*: "don't call us, we'll call you."

This paradigm underlies GUI toolkits (every button click handler is event-driven), JavaScript's browser and Node.js runtime, and any system built around callbacks, listeners, or an event loop.

<sub>[Back to top](#table-of-contents)</sub>

---

## Callbacks, Listeners, and the Event Loop

- ### Callbacks and Listeners:
  A callback is a function passed to be invoked later, when an event occurs. A listener is the same idea named for its role — "listening" for a specific event type on a specific source (a button, a socket, an emitter).

  ```javascript
  button.addEventListener('click', (event) => {
    console.log('Clicked at', event.clientX, event.clientY);
  });
  ```

- ### The Event Loop:
  Runtimes built around event-driven programming (Node.js, browser JavaScript) use a single-threaded event loop: it continuously checks a queue for pending events/callbacks and runs them one at a time. This is why long-running synchronous code blocks everything else — there's no preemption, only cooperative handoff back to the loop.

  ```mermaid
  flowchart LR
      A[Event Queue] --> B{Event Loop}
      B -->|dequeue| C[Run Handler]
      C -->|handler returns| B
      D[I/O, Timer, User Action] -->|enqueues event| A
  ```

  **Caption:** New events are enqueued as they occur; the event loop dequeues and runs one handler at a time.

<sub>[Back to top](#table-of-contents)</sub>

---

## Programming-Model vs. Architectural-Style

"Event-driven" shows up at two different altitudes in this repo, and it's worth keeping them distinct: this page covers the *programming-model* level — how a single program's control flow is structured around callbacks/listeners/an event loop. The [Event-Driven Architecture](../../architectural-patterns/message-driven/event-driven.md) page covers the *system-level* pattern — independent services communicating asynchronously via events through a broker. The architectural pattern is, in a sense, event-driven programming applied at the scale of a distributed system.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

**Q: Why does one blocking call freeze an entire Node.js server?**
A: Node's event loop is single-threaded — while a synchronous, CPU-bound call is running, the loop can't dequeue or process any other pending event, so every other client's callback waits.

---

**Q: How does event-driven programming relate to Reactive Programming?**
A: Reactive programming builds on the same event/callback foundation but adds a rich set of composable operators (map, filter, debounce, combine) for working with streams of events declaratively, rather than wiring up individual callbacks by hand.

---

**Q: Is event-driven programming inherently asynchronous?**
A: Usually, but not by definition — the defining trait is that control flow is triggered by events rather than sequential execution; most real event-driven systems pair that with asynchronous, non-blocking I/O to avoid stalling the event loop, but the two concepts are separable.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Reactive Programming](reactive.md) — builds composable stream operators on top of the same event/callback foundation
- [Event-Driven Architecture](../../architectural-patterns/message-driven/event-driven.md) — the same idea applied at the scale of independent, distributed services
- [Concurrent Programming](concurrent.md) — event loops are one strategy (alongside threads) for handling concurrent work

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [MDN: Introducing asynchronous JavaScript](https://developer.mozilla.org/en-US/docs/Learn/JavaScript/Asynchronous) — official documentation
- [Node.js: The Event Loop](https://nodejs.org/en/docs/guides/event-loop-timers-and-nexttick/) — official documentation

---

[Get Started](../../../get-started.md#paradigms)

---
