# PHP

---

## Table of Contents
<!-- TOC -->
* [PHP](#php)
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

PHP is a dynamically typed scripting language purpose-built for server-side web development, originally standing for "Personal Home Page" and now recursively for "PHP: Hypertext Preprocessor." Its defining architectural trait is a request-per-process (shared-nothing) execution model: each incoming HTTP request typically gets a fresh, isolated PHP process or thread that starts clean and discards all state when the response is sent. Despite predictions of decline, modern PHP (8.x) powers a large share of the web, including WordPress and major frameworks like Laravel and Symfony.

---

## Overview

PHP emerged in the mid-1990s as a set of tools for building dynamic web pages and grew into a full general-purpose language still deeply tied to the web request/response lifecycle. It is dynamically typed, interpreted, and designed to be embedded directly within HTML — a `.php` file can freely mix markup and server-side logic.

Historically criticized for inconsistent standard library naming and loose typing pitfalls, PHP has modernized substantially since PHP 7 and especially PHP 8: it added a JIT compiler, union and nullable types, enums, attributes, and better performance, narrowing much of the gap that once separated it from newer server-side languages.

<sub>[Back to top](#table-of-contents)</sub>

---

## Paradigm

PHP started as an imperative, procedural scripting language and layered in a full object-oriented model over time (classes, interfaces, traits, and — as of PHP 8 — enums and readonly properties). Modern PHP code, especially in frameworks like Laravel and Symfony, is predominantly OOP, though procedural code remains common in simpler scripts and legacy codebases.

The architecturally distinctive trait is not the paradigm but the **execution model**: PHP traditionally follows a shared-nothing, request-per-process approach — a web server (via PHP-FPM, a FastCGI process manager) spins up a process (or reuses one from a pool) to handle each request, executes the script from scratch, and tears down all in-memory state afterward. This contrasts with Node.js, where a single long-lived process keeps an event loop running and in-memory state persists across requests unless explicitly discarded.

```php
<?php
// Each request re-initializes state — nothing here persists to the next request
// unless it is written to a database, cache, or session store.
$counter = 0;
$counter++;
echo $counter; // always 1, every request
```

This shared-nothing model has real architectural upside: a crashing request can't corrupt shared in-memory state for other requests, and horizontal scaling is straightforward since processes are inherently stateless. The trade-off is that any state that needs to persist (sessions, caches, connections) must be externalized to something like a database, Redis, or the session mechanism, and per-request bootstrapping (autoloading, framework init) adds overhead that a persistent process avoids.

<sub>[Back to top](#table-of-contents)</sub>

---

## Typical Use Cases

- **Server-side web applications** — PHP's original and still primary domain, from simple scripts to full frameworks.
- **Content management systems** — WordPress, Drupal, and similar platforms are PHP-based and represent a large share of the web.
- **REST APIs and web backends** — via modern frameworks such as Laravel and Symfony (named, not taught here).
- **Shared hosting environments** — the request-per-process model and broad hosting support make PHP a natural fit for low-ops deployment on commodity web hosts.

<sub>[Back to top](#table-of-contents)</sub>

---

## Runtime and Deployment

PHP scripts are interpreted by the Zend Engine at request time; PHP 8 added an opcache-integrated JIT compiler that can improve CPU-bound performance, though most web workloads remain I/O-bound and benefit more from opcode caching than JIT compilation. Deployment typically pairs a web server (Nginx or Apache) with PHP-FPM, which manages a pool of worker processes that handle requests — scaling is achieved by increasing pool size or adding more servers, not by managing concurrency within a single long-lived process.

This is the key contrast with Node.js: where Node keeps one process alive indefinitely and multiplexes many requests through its event loop, PHP-FPM cycles through a pool of processes, each handling one request at a time from a clean state. Modern deployments increasingly containerize PHP-FPM alongside the web server, and some environments (e.g. Swoole, RoadRunner) offer long-running, event-loop-style PHP runtimes as an alternative to the traditional model.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ecosystem

Composer is PHP's standard dependency and package manager, resolving and autoloading packages from Packagist, PHP's central package registry. Laravel and Symfony are the two dominant modern frameworks (named here, not taught), providing routing, ORM, templating, and the conventions most new PHP projects are built on. PHPUnit is the standard testing framework.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

**Q: Why would per-request process isolation ever be preferable to Node's persistent event loop?**
A: It trades raw efficiency for fault isolation and simplicity: a memory leak, unhandled exception, or corrupted in-memory state in one request can't leak into the next, because every request starts from zero. That removes an entire category of "state slowly rotting in a long-lived process" bugs, at the cost of re-paying process/bootstrap overhead on every request.

---

**Q: Does the shared-nothing model mean PHP applications can't maintain state between requests at all?**
A: They can't keep it in PHP process memory, but they can and do maintain state externally — in a database, a cache like Redis or Memcached, or the built-in session mechanism (which persists a session identifier via cookie and looks up server-side state per request). The state just has to live somewhere that survives process teardown.

---

**Q: Is PHP still a reasonable choice for a new backend service today?**
A: Yes, particularly if the team already has PHP expertise, the target is a fairly standard web application, or ease of hosting/deployment matters — modern PHP 8 with Laravel or Symfony is a mature, well-performing stack. It's a less natural fit than Node.js or Python for I/O-heavy real-time features (WebSockets, long-lived connections) without adopting one of PHP's newer event-loop runtimes.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [JavaScript](javascript.md) — Node.js's persistent event loop is the direct architectural contrast to PHP's request-per-process model
- [Python](python.md) — another dynamic scripting language, but typically deployed as long-running WSGI/ASGI processes rather than per-request
- [Ruby](ruby.md) — a dynamic, web-oriented language with a comparable framework-driven ecosystem identity (Rails vs. Laravel/Symfony)
- [Object-Oriented Programming](../paradigms/object-oriented.md) — the OOP model modern PHP frameworks are built around

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [PHP Manual](https://www.php.net/manual/en/) — official language and standard library documentation
- [PHP-FPM Documentation](https://www.php.net/manual/en/install.fpm.php) — official documentation on the FastCGI Process Manager

---

[Get Started](../../../get-started.md) | [Programming Languages](../../../get-started.md#languages)

---
