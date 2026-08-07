# Angular

---

## Table of Contents
<!-- TOC -->
* [Angular](#angular)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [A Full, Opinionated Framework](#a-full-opinionated-framework)
  * [TypeScript-First Design](#typescript-first-design)
  * [Components, Services, and Dependency Injection](#components-services-and-dependency-injection)
  * [Two-Way Data Binding](#two-way-data-binding)
  * [Key Concepts](#key-concepts)
  * [Examples](#examples)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

**Angular** is a complete, opinionated web application framework maintained by Google, rebuilt from scratch in 2016 (as "Angular", distinct from the earlier AngularJS) around TypeScript, components, and dependency injection. Where React hands architectural decisions to the team, Angular makes most of them itself — routing, HTTP, forms, and dependency injection all ship as first-party modules with a prescribed way of using them. This makes Angular the natural choice when an organization wants many teams to converge on the same application structure with minimal setup decisions.

---

## Overview

Angular's defining trait is that it is a **framework**, not a library: it dictates project structure, provides its own CLI (`ng new`, `ng generate`) to scaffold consistent code, and bundles routing, forms, HTTP client, and testing utilities as official, maintained packages rather than leaving them to third parties. This "batteries-included" philosophy trades flexibility for consistency — large organizations with many teams and long-lived codebases often prefer this, since it reduces the number of ecosystem decisions any given team can make differently.

Angular has always been TypeScript-first, and its component model leans heavily on **dependency injection (DI)**, a pattern more commonly associated with backend frameworks like Spring than with frontend UI libraries. Understanding Angular's DI system is arguably more valuable to an architect than understanding its templating syntax, because the same IoC principles reappear across backend and frontend stacks.

<sub>[Back to top](#table-of-contents)</sub>

---

## A Full, Opinionated Framework

- ### Batteries Included:
  A default Angular application ships with an opinionated router, HTTP client, forms module (template-driven and reactive), and CLI-driven build tooling, all versioned and released together. Contrast this with [React](react.md), which supplies none of these — a team choosing React must separately pick a router and state library, whereas an Angular team uses `@angular/router` and `@angular/forms` by default.

<sub>[Back to top](#table-of-contents)</sub>

- ### Consistency Across Teams:
  Because the CLI scaffolds components, services, and modules in a fixed shape, Angular codebases across different teams tend to look structurally similar. This lowers onboarding cost when engineers move between projects, at the cost of flexibility to deviate from the framework's conventions.

<sub>[Back to top](#table-of-contents)</sub>

---

## TypeScript-First Design

- ### Built On, Not Bolted Onto, TypeScript:
  Angular's own source is written in TypeScript, and its APIs (decorators, generics in the DI system, strict template type-checking) are designed assuming TypeScript from the outset — it is not an optional layer added later, unlike React or Vue, which both started as JavaScript libraries that TypeScript support was retrofitted onto.

  ```typescript
  @Component({
    selector: 'app-counter',
    template: `<button (click)="increment()">Clicked {{ count }} times</button>`
  })
  export class CounterComponent {
    count = 0;
    increment(): void {
      this.count++;
    }
  }
  ```

<sub>[Back to top](#table-of-contents)</sub>

---

## Components, Services, and Dependency Injection

- ### Components:
  An Angular component pairs a TypeScript class (logic and state) with an HTML template (view) and optional CSS, declared via the `@Component` decorator. Components form a tree, similarly to React, but each also declares its dependencies explicitly through its constructor.

<sub>[Back to top](#table-of-contents)</sub>

- ### Services and Injectable Dependency Injection:
  Cross-cutting logic (HTTP calls, shared state, logging) lives in **services** — plain classes decorated with `@Injectable()`. Angular's built-in DI container constructs and supplies these services to whatever component or service asks for them via its constructor, rather than components instantiating their own dependencies.

  ```typescript
  @Injectable({ providedIn: 'root' })
  export class UserService {
    constructor(private http: HttpClient) {}
    getUser(id: string) {
      return this.http.get(`/api/users/${id}`);
    }
  }

  @Component({ selector: 'app-user', template: '...' })
  export class UserComponent {
    constructor(private userService: UserService) {}
  }
  ```

  ```mermaid
  flowchart LR
      Injector[DI Container / Injector] -->|provides| UserService
      Injector -->|provides| HttpClient
      UserService -->|injected into| UserComponent
      HttpClient -->|injected into| UserService
  ```

  **Caption:** Angular's injector constructs services and supplies them to whatever class declares a dependency on them via its constructor.

  This is architecturally the same **Inversion of Control** pattern used by Spring on the backend: classes declare what they need, a container resolves and wires the object graph, and dependencies are swappable (e.g., for testing) by providing a different implementation to the injector rather than editing consuming code. An architect fluent in Spring's DI model will find Angular's constructor injection immediately familiar.

<sub>[Back to top](#table-of-contents)</sub>

---

## Two-Way Data Binding

- ### `[(ngModel)]` and Bidirectional Sync:
  Angular's forms module historically popularized **two-way data binding**: a form control and a component property stay automatically in sync in both directions — typing in an input updates the bound property, and programmatically changing the property updates the input.

  ```html
  <input [(ngModel)]="username" />
  <p>Hello, {{ username }}</p>
  ```

  This is syntactic sugar over separate property binding (`[value]`) and event binding (`(input)`), combined with the "banana in a box" `[( )]` syntax.

<sub>[Back to top](#table-of-contents)</sub>

- ### Historical Significance and Trade-offs:
  Two-way binding made simple forms very fast to write, and it's part of what made the original AngularJS popular. But at scale it can make data flow harder to trace than the explicit, one-way flow used by React and by Angular's own reactive forms API, since a value can change from either the template or the component without an obvious single trigger. Modern Angular guidance increasingly favors reactive forms and signals for predictable state changes, while keeping `ngModel` available for simple cases.

<sub>[Back to top](#table-of-contents)</sub>

---

## Key Concepts

| Term | Definition |
|------|------------|
| Component | Class + template pairing that renders a piece of UI, declared with `@Component` |
| Service | Injectable class holding shared logic or state, decorated with `@Injectable()` |
| Dependency Injection (DI) | Pattern where a container constructs and supplies a class's dependencies rather than the class creating them itself |
| Module (`NgModule`) | Angular's original unit of grouping related components/services (largely superseded by standalone components) |
| Two-way data binding | `[(ngModel)]` syntax that keeps a template control and a component property in sync in both directions |
| Angular CLI | Official scaffolding and build tool (`ng generate`, `ng build`) enforcing consistent project structure |

<sub>[Back to top](#table-of-contents)</sub>

---

## Examples

A component that injects a service and binds a form field with two-way binding:

```typescript
@Component({
  selector: 'app-profile',
  template: `
    <input [(ngModel)]="name" placeholder="Your name" />
    <button (click)="save()">Save</button>
  `
})
export class ProfileComponent {
  name = '';
  constructor(private userService: UserService) {}

  save(): void {
    this.userService.updateName(this.name);
  }
}
```

`ProfileComponent` never constructs `UserService` itself — Angular's injector supplies it — and `name` stays synchronized with the input automatically via `ngModel`, without an explicit change handler.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Why would an organization choose Angular's opinionated structure over React's flexibility?**
A: When many teams need to build interchangeable, maintainable applications and onboarding speed matters more than per-project flexibility, Angular's enforced structure (CLI-scaffolded components/services, mandated router and DI patterns) reduces architectural drift between projects. React optimizes for the opposite: maximum flexibility per team, at the cost of consistency across teams.

---

**Q: How does Angular's dependency injection compare to Spring's?**
A: Both implement the Inversion of Control pattern: a container resolves a class's declared dependencies (constructor parameters in Angular, constructor or field injection in Spring) and supplies concrete implementations, which can be swapped — e.g., mocked in tests — without touching the consuming class. Angular's injector is per-application (or per-component-tree) and resolves TypeScript classes; Spring's is typically per-application-context and resolves Java beans, but the underlying mental model is the same.

---

**Q: Is two-way data binding still recommended in modern Angular applications?**
A: It remains available and convenient for small forms, but Angular's own guidance leans toward reactive forms and signals for larger applications, because explicit, traceable data flow is easier to debug at scale than bidirectional sync — the same trade-off that led React to standardize on unidirectional data flow from the start.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [React](react.md) — the library-only alternative Angular is most often contrasted with
- [Vue.js](vuejs.md) — a progressive framework positioned between React's minimalism and Angular's all-in structure
- [Frontend Styling & Tooling](frontend-styling-tooling.md) — CSS preprocessors and component libraries commonly used alongside Angular

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Angular Documentation](https://angular.dev/) — official documentation
- [Angular Dependency Injection Guide](https://angular.dev/guide/di) — official guide to Angular's DI system

---

[Get Started](../../../get-started.md) | [Web Development Frameworks](../../../get-started.md#web-development-frameworks)

---
