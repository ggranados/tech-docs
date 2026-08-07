# Ruby

---

## Table of Contents
<!-- TOC -->
* [Ruby](#ruby)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Paradigm](#paradigm)
  * [Typical Use Cases](#typical-use-cases)
  * [Runtime and Deployment](#runtime-and-deployment)
  * [Ecosystem](#ecosystem)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

Ruby is a dynamically typed, multi-paradigm language designed around programmer happiness and expressiveness, built on the principle that "everything is an object" — even integers and `nil` respond to methods. It is inseparable in most architects' minds from Ruby on Rails, the web framework that popularized it and whose "convention over configuration" philosophy went on to influence a generation of frameworks in other languages. This overview focuses on Ruby the language and its ecosystem identity, not Rails internals.

---

## Overview

Created by Yukihiro "Matz" Matsumoto and released in 1995, Ruby was explicitly designed to be enjoyable and readable for the developer, blending ideas from Perl, Smalltalk, and Lisp. Matz has described prioritizing programmer happiness over pure performance or minimalism — Ruby favors expressive, human-readable syntax, often providing multiple ways to express the same intent.

Ruby is dynamically and strongly typed, interpreted by default (the reference implementation is CMRuby/MRI, "Matz's Ruby Interpreter"), with alternative implementations like JRuby (JVM-based) and TruffleRuby (GraalVM-based) offering different performance and interoperability trade-offs. Like Python's CPython, MRI has a Global Interpreter Lock (GIL, called the GVL in Ruby) that limits true parallel thread execution, pushing CPU-bound concurrency toward multiprocessing or alternative interpreters.

<sub>[Back to top](#table-of-contents)</sub>

---

## Paradigm

Ruby is multi-paradigm but leans OOP more consistently and purely than most dynamic languages: literally everything is an object, including classes themselves (which are instances of `Class`), numbers, and even `nil`/`true`/`false`. There are no primitive types outside the object model, which keeps the language's mental model unusually uniform.

Ruby also supports functional-style programming (blocks, procs, lambdas as first-class values, and enumerable methods like `map`/`select`/`reduce`) and offers powerful metaprogramming facilities (open classes, `method_missing`, dynamic method definition) that let code modify its own structure at runtime — a capability Rails leans on heavily to implement its conventions and DSLs.

```ruby
# Everything is an object, including numbers
5.times { |i| puts "iteration #{i}" }

# Blocks make iteration and functional-style composition idiomatic
[1, 2, 3].map { |n| n * 2 }.select { |n| n > 2 } # => [4, 6]
```

<sub>[Back to top](#table-of-contents)</sub>

---

## Typical Use Cases

- **Web applications** — overwhelmingly via Ruby on Rails, which shaped Ruby's identity as a web-first language.
- **Scripting and automation** — text processing, build tooling (Rake), and general-purpose scripting.
- **Rapid prototyping / MVPs** — Rails' scaffolding and conventions are specifically optimized for getting a working product quickly.
- **DSL-heavy tooling** — Ruby's flexible syntax and metaprogramming make it a common choice for internal domain-specific languages (e.g. Chef, Puppet historically used Ruby DSLs).

<sub>[Back to top](#table-of-contents)</sub>

---

## Runtime and Deployment

MRI compiles Ruby source to an internal bytecode representation and interprets it on a virtual machine (YARV) at runtime; there is no ahead-of-time compilation to a native binary in typical deployments. Ruby applications, like Node.js and Python services, are deployed as long-running processes — commonly behind an application server (Puma, Unicorn) that manages a pool of worker processes, often with a reverse proxy (Nginx) in front.

Because of the GVL, a single Ruby process does not achieve true multi-core parallelism for CPU-bound work; horizontal scaling via multiple worker processes (and, within each, fibers or threads for I/O-bound concurrency) is the standard approach, mirroring Python's process-pool pattern more than Node's single-process event loop.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ecosystem

RubyGems is the standard package manager and registry ("gems"), with Bundler managing project-level dependency resolution and version locking via a `Gemfile`. Ruby on Rails is the dominant framework and the primary reason for Ruby's continued relevance — its "convention over configuration" and "don't repeat yourself" principles (sensible defaults over explicit setup, e.g. inferring database table and column mappings from class and method names) reduced boilerplate so effectively that later frameworks in other languages (Laravel in PHP, Django in Python to a degree) borrowed the same philosophy.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

**Q: Is it fair to say Ruby and Rails are the same thing architecturally?**
A: No, though the perception is understandable — Rails is by far Ruby's most influential use case and most companies encounter Ruby through it. But Ruby is a general-purpose language usable for scripting, tooling, and non-Rails frameworks (Sinatra, Hanami); Rails is one (extremely influential) application of the language, not the language itself.

---

**Q: What does "convention over configuration" actually buy an architect, versus a framework that requires explicit config?**
A: It drastically cuts boilerplate and decision fatigue for the common case — Rails infers database mappings, routes, and file locations from naming conventions instead of requiring explicit wiring. The trade-off is less flexibility and a steeper cost when a project's needs diverge from the conventions, since fighting the framework's assumptions is often harder than configuring an explicit setup from scratch.

---

**Q: When would I reach for Ruby over Python for a new project?**
A: If the target is a web application and the team values Rails' rapid-development conventions and mature ecosystem for CRUD-heavy apps, Ruby/Rails is a strong choice. Python tends to win when the project touches data science/ML, needs a broader general-purpose ecosystem beyond web, or the team already has stronger Python expertise — the two languages are close enough in paradigm and runtime shape that the choice often comes down to ecosystem fit and team familiarity rather than language capability.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Python](python.md) — a similarly dynamic, multi-paradigm, GIL-constrained language, but with a more pragmatic (less purely OOP) design philosophy
- [PHP](php.md) — another web-framework-driven ecosystem identity (Rails vs. Laravel/Symfony), with a different execution model
- [JavaScript](javascript.md) — a dynamic language with prototypal rather than class-based OOP, and an event-loop concurrency model instead of a GVL
- [Object-Oriented Programming](../paradigms/object-oriented.md) — the "everything is an object" model Ruby applies more consistently than most languages
- [Functional Programming](../paradigms/functional.md) — blocks, procs, and lambdas that support Ruby's functional-style idioms

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Ruby Documentation](https://www.ruby-lang.org/en/documentation/) — official language documentation
- [Ruby on Rails Guides](https://guides.rubyonrails.org/) — official Rails framework guides

---

[Get Started](../../../get-started.md) | [Programming Languages](../../../get-started.md#languages)

---
