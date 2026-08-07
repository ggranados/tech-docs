# HTML and CSS

---

## Table of Contents
<!-- TOC -->
* [HTML and CSS](#html-and-css)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [HTML](#html)
  * [CSS](#css)
  * [Typical Use Cases](#typical-use-cases)
  * [Runtime and Deployment](#runtime-and-deployment)
  * [Ecosystem](#ecosystem)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

HTML and CSS are not general-purpose programming languages — HTML is a markup language for structuring content, and CSS is a style-sheet language for presenting it. Together they form the two foundational, declarative building blocks of every web page, alongside JavaScript for behavior. An architect doesn't need to master either deeply, but should understand the mental models each is built on: the semantic document tree for HTML, and the cascade/box model for CSS.

---

## Overview

HTML (HyperText Markup Language) describes the structure and meaning of content using nested elements — headings, paragraphs, lists, forms — that browsers parse into the **DOM (Document Object Model)**, a tree structure that JavaScript and CSS both operate on. CSS (Cascading Style Sheets) then describes how that structure should be presented: colors, spacing, layout, and responsive behavior across screen sizes.

Both are maintained as open web standards by the **W3C** and the **WHATWG**, and both are declarative — you describe the desired structure or appearance, and the browser's rendering engine figures out how to produce it, in contrast to imperative UI code that manipulates pixels or widgets directly.

<sub>[Back to top](#table-of-contents)</sub>

---

## HTML

- ### Semantic Structure:
  Modern HTML favors **semantic elements** (`<header>`, `<nav>`, `<main>`, `<article>`, `<section>`, `<footer>`) over generic `<div>` soup. Semantic markup communicates document meaning to browsers, search engines, and assistive technology (screen readers), which is central to accessibility and SEO.

  ```html
  <article>
    <header><h1>Post Title</h1></header>
    <p>Body content...</p>
  </article>
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### The DOM:
  The browser parses HTML into the DOM — a live, in-memory tree of nodes. This is the structure CSS selectors match against and the structure JavaScript reads and mutates, making it the shared foundation the rest of the front-end stack builds on.

<sub>[Back to top](#table-of-contents)</sub>

---

## CSS

- ### The Box Model:
  Every rendered HTML element is treated as a rectangular box made of **content, padding, border, and margin**, nested in that order from the inside out. Understanding this model is the single biggest unlock for predictable layout — most "why is there unexpected spacing" issues trace back to it.

  ```css
  .card {
    padding: 16px;
    border: 1px solid #ccc;
    margin: 8px;
  }
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### Cascade and Specificity:
  When multiple rules target the same element, CSS resolves conflicts through the **cascade** — a priority order based on origin (browser vs. author styles), specificity (how precise a selector is: ID > class > element), and source order (later rules win ties). Understanding specificity is essential for predicting which rule actually applies.

  ```css
  #hero { color: blue; }     /* higher specificity — wins */
  .banner { color: red; }
  ```

<sub>[Back to top](#table-of-contents)</sub>

- ### Modern Layout:
  **Flexbox** (one-dimensional layout — rows or columns) and **Grid** (two-dimensional layout — rows and columns together) are the modern standards for arranging elements, largely superseding older float- and table-based layout hacks. Both are declarative: you describe the desired arrangement and let the browser compute positions.

<sub>[Back to top](#table-of-contents)</sub>

- ### Preprocessors:
  Tools like **Sass** and **LESS** extend CSS with variables, nesting, and mixins, compiling down to plain CSS at build time. They're covered under [Frontend Frameworks](../../../get-started.md#frontend) in this repo rather than here.

<sub>[Back to top](#table-of-contents)</sub>

---

## Typical Use Cases

HTML and CSS are the baseline of every web-based user interface — from static marketing pages to the rendered output of single-page applications built with React, Angular, or Vue. Even frameworks that abstract markup generation (JSX, templates) ultimately compile down to HTML and CSS that the browser renders, so understanding both remains foundational regardless of which framework a team uses on top.

<sub>[Back to top](#table-of-contents)</sub>

---

## Runtime and Deployment

HTML and CSS have no runtime in the traditional sense — there's no compiler output or process to deploy. Instead, the browser's **rendering engine** (e.g., Blink, WebKit, Gecko) parses HTML into the DOM, parses CSS into the CSSOM, and combines both into a render tree that is laid out and painted to the screen. "Deployment" simply means serving the files (often via a CDN) for the browser to fetch and render, with no server-side execution required for the markup and styles themselves.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ecosystem

Beyond preprocessors (Sass, LESS), the surrounding ecosystem includes CSS methodologies for maintainability at scale (BEM naming conventions, utility-first frameworks like Tailwind CSS), and browser developer tools for live-inspecting the DOM/CSSOM. Build tools (bundlers, autoprefixers via PostCSS) commonly process CSS before it ships to handle vendor prefixes and browser compatibility.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: Why does specificity matter architecturally, not just as a CSS quirk?**
A: On large front-end codebases, unpredictable specificity conflicts are a major source of "why won't this style apply" bugs and lead teams to reach for `!important` as a workaround, which compounds the problem. Conventions like BEM or scoped/CSS-in-JS styling exist specifically to keep specificity flat and predictable as a codebase grows.

---

**Q: When would you reach for Grid instead of Flexbox?**
A: Flexbox is suited to one-dimensional arrangements — a row of nav items or a column of cards. Grid is suited to two-dimensional layouts where you need to control rows and columns simultaneously, like a full page layout with a header, sidebar, and content area. They're complementary, not competing.

---

**Q: Are HTML and CSS "real" languages an architect needs to evaluate like a programming language?**
A: They're declarative markup/style languages, not general-purpose or Turing-complete (CSS has gained some programmability via custom properties and `calc()`, but that's not its core design). The architectural decisions that matter are less about the languages themselves and more about the surrounding system: component structure, styling methodology, and build tooling.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [TypeScript](typescript.md) — typically the third leg alongside HTML/CSS in a modern front end
- [Declarative Programming](../paradigms/declarative.md) — the paradigm both HTML and CSS embody
- [Object Oriented Programming](../paradigms/object-oriented.md) — the DOM itself is exposed to scripting languages as an object model

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [HTML — MDN Web Docs](https://developer.mozilla.org/en-US/docs/Web/HTML) — authoritative HTML reference
- [CSS — MDN Web Docs](https://developer.mozilla.org/en-US/docs/Web/CSS) — authoritative CSS reference

---

[Get Started](../../../get-started.md) | [Programming Languages](../../../get-started.md#languages)

---
