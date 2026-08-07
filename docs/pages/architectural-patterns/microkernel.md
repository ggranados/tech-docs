# Microkernel Architecture (Plug-in Architecture)

---

## Table of Contents
<!-- TOC -->
* [Microkernel Architecture (Plug-in Architecture)](#microkernel-architecture-plug-in-architecture)
  * [Table of Contents](#table-of-contents)
  * [Overview](#overview)
  * [Core System and Plug-in Modules](#core-system-and-plug-in-modules)
  * [Plug-in Registration Mechanisms](#plug-in-registration-mechanisms)
  * [Key Concepts](#key-concepts)
  * [Examples](#examples)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

---

**Microkernel Architecture**, also called **Plug-in Architecture**, structures an application around a minimal **core system** that provides only essential functionality, with additional features implemented as independent **plug-in modules** that the core discovers and invokes at runtime. Unlike microservices, plug-ins run in the same process as the core — there is no network boundary between them, only a well-defined internal contract.

---

## Overview

The pattern takes its name from the operating-system microkernel design, where the kernel handles only the bare essentials (memory management, process scheduling, inter-process communication) and everything else — device drivers, file systems, networking — runs as separate modules on top of it. Applied to application architecture, the same idea produces systems like IDEs (Eclipse's plug-in system), browsers (extension APIs), and rules engines, where a small stable core is extended almost entirely through plug-ins rather than by modifying the core itself.

The motivation is extensibility without instability: the core system stays small, well-tested, and rarely changes, while new capabilities are added by writing new plug-ins that conform to the core's contract. This isolates the blast radius of a new feature — a broken plug-in typically doesn't take down the whole application — and lets third parties extend the system without access to (or the need to understand) its internals.

Microkernel architecture is a good fit when a product needs to support optional or customer-specific features, a marketplace of extensions, or incremental feature rollout, without bloating a shared core or requiring a full redeploy of the base system for every new capability.

<sub>[Back to top](#table-of-contents)</sub>

---

## Core System and Plug-in Modules

The architecture has exactly two kinds of participants: one core, and any number of plug-ins.

- ### Core System:
  Contains the minimal logic required to run the application and the machinery to discover, load, and invoke plug-ins — a plug-in registry, lifecycle hooks, and the contract (interfaces) plug-ins must implement. The core does not know about specific plug-ins in advance.

- ### Plug-in Modules:
  Independent units of functionality that implement the core's contract and are registered with it, either at startup (static configuration, classpath scanning) or dynamically at runtime (a marketplace installing a new plug-in without restarting the app).

  ```mermaid
  flowchart TB
      subgraph Core["Core System"]
          Registry["Plug-in Registry"]
          Contract["Plug-in Contract (interface)"]
          Essential["Essential Services"]
      end

      subgraph Plugins["Plug-in Modules (in-process)"]
          P1["Export-to-PDF Plug-in"]
          P2["Spell-Check Plug-in"]
          P3["Custom Report Plug-in"]
      end

      Registry --> P1
      Registry --> P2
      Registry --> P3
      P1 -.implements.-> Contract
      P2 -.implements.-> Contract
      P3 -.implements.-> Contract
  ```

  **Caption:** All plug-ins implement the same core contract and run inside the same process as the core, which discovers and invokes them through its registry.

<sub>[Back to top](#table-of-contents)</sub>

---

## Plug-in Registration Mechanisms

How the core discovers and invokes plug-ins is the crux of the pattern's implementation.

- ### Strategy Pattern as the Typical Mechanism:
  Each plug-in commonly implements a shared interface — exactly the shape of the **Strategy** design pattern — and the core holds a collection of registered strategies it can select and invoke at runtime without knowing their concrete types. Registration is often as simple as adding an implementation to the registry (a map, a service-loader list, a DI container's multi-binding).

  > See also: [Strategy Pattern](../design-patterns/behavioral/strategy.md)

- ### Command Pattern for Invocation:
  When plug-ins represent discrete actions to be triggered (a menu action, a pipeline step), the core frequently wraps each plug-in invocation as a **Command** object, letting it queue, log, undo, or schedule plug-in execution uniformly regardless of which plug-in is behind it.

  > See also: [Command Pattern](../design-patterns/behavioral/command.md)

- ### Contrast with Microservices:
  Microkernel plug-ins run **in-process** with the core — they are loaded into the same runtime, share memory, and are invoked through direct method calls, so there is no network latency, no independent deployment, and no partial-failure/network-resilience concern between core and plug-in. Microservices instead split functionality into **independently deployed, independently scaled** processes communicating over the network. Microkernel trades microservices' deployment independence and fault isolation for simplicity: extending the system is as easy as adding a class that implements an interface, not standing up a new deployable service.

  > See also: [Microservices Architecture](microservices.md)

<sub>[Back to top](#table-of-contents)</sub>

---

## Key Concepts

| Term | Definition |
|------|------------|
| Core System | The minimal, stable part of the application, including the plug-in contract and registry |
| Plug-in | An independently developed module implementing the core's contract, loaded into the same process |
| Plug-in Registry | The mechanism the core uses to discover, hold, and invoke registered plug-ins |
| Static Registration | Plug-ins wired in at build/startup time (classpath scanning, DI configuration) |
| Dynamic Registration | Plug-ins discovered and loaded at runtime without restarting the core |

<sub>[Back to top](#table-of-contents)</sub>

---

## Examples

A minimal core using the Strategy pattern to invoke plug-ins registered through a simple registry:

```java
// Contract every plug-in must implement
public interface ExportPlugin {
    String format();
    byte[] export(Document doc);
}

// Core registry — holds registered strategies, agnostic of concrete plug-in types
public class ExportPluginRegistry {
    private final Map<String, ExportPlugin> plugins = new HashMap<>();
    public void register(ExportPlugin plugin) { plugins.put(plugin.format(), plugin); }
    public byte[] export(String format, Document doc) {
        return plugins.get(format).export(doc); // core never knows PdfExportPlugin, etc.
    }
}

// A concrete plug-in, developed and registered independently of the core
public class PdfExportPlugin implements ExportPlugin {
    public String format() { return "pdf"; }
    public byte[] export(Document doc) { /* render to PDF */ return new byte[0]; }
}
```

The core's `ExportPluginRegistry` never changes when a new export format is added — a new class implementing `ExportPlugin` is written and registered, leaving the stable core untouched.

<sub>[Back to top](#table-of-contents)</sub>

---

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: How is Microkernel architecture different from Microservices, if both are about splitting functionality into independent units?**
A: The independence is of a different kind. Microkernel plug-ins are independent *modules of code* implementing a shared contract, but they run in-process with the core and are invoked through direct method calls — no network, no independent deployment. Microservices split functionality into independently deployed, independently scaled processes communicating over a network. Microkernel is about extensibility within one runtime; Microservices is about organizational and operational independence across many runtimes.

---

**Q: What design pattern is most commonly used to implement plug-in registration, and why?**
A: The Strategy pattern is the natural fit: each plug-in implements a common interface, and the core holds a collection of these interchangeable implementations it can select at runtime without depending on any concrete plug-in type. When plug-ins represent discrete triggerable actions rather than swappable algorithms, the Command pattern is often layered on top to standardize how those actions are invoked, queued, or undone.

---

**Q: What happens if a plug-in crashes — does it take down the whole application?**
A: It can, because plug-ins share the core's process and memory space. A well-built microkernel system mitigates this with defensive measures — wrapping plug-in invocations in exception handling at the registry level, running risky plug-ins in a sandboxed thread or process, or validating plug-ins before registration — but the in-process nature of the pattern means fault isolation is never as strong as it is between independently deployed microservices.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Microservices Architecture](microservices.md) — contrast: in-process, shared-runtime plug-ins vs. independently deployed, networked services
- [Strategy Pattern](../design-patterns/behavioral/strategy.md) — the typical mechanism by which plug-ins implement a shared, swappable contract
- [Command Pattern](../design-patterns/behavioral/command.md) — commonly used to wrap and standardize plug-in invocation as discrete actions
- [Hexagonal Architecture (Ports and Adapters)](hexagonal.md) — both patterns isolate a stable core behind interfaces, though Hexagonal centers on infrastructure independence rather than runtime extensibility

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- [Microkernel Pattern — Microsoft Azure Architecture Center](https://learn.microsoft.com/en-us/azure/architecture/patterns/) — pattern catalog covering plug-in and extensibility architectures
- [Eclipse Plug-in Architecture Overview](https://www.eclipse.org/articles/Article-Plug-in-architecture/plugin_architecture.html) — a widely cited real-world microkernel implementation

---

[Get Started](../../get-started.md) | [Architectural Patterns](../../get-started.md#architectural-patterns)

---
