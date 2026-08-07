# OOP in Java


---

## Table of Contents
<!-- TOC -->
* [OOP in Java](#oop-in-java)
  * [Table of Contents](#table-of-contents)
  * [What Is a Class?](#what-is-a-class)
  * [What Is an Object?](#what-is-an-object)
  * [What Is Inheritance?](#what-is-inheritance)
  * [Encapsulation and Data Hiding](#encapsulation-and-data-hiding)
    * [Encapsulation](#encapsulation)
    * [Data Hiding](#data-hiding)
    * [Example](#example)
  * [What Is an Interface?](#what-is-an-interface)
  * [Polymorphism](#polymorphism)
    * [Method Overriding](#method-overriding)
    * [Method Overloading](#method-overloading)
      * [Example](#example-1)
  * [What Is a Package?](#what-is-a-package)
  * [Q&A](#qa)
  * [Related Topics](#related-topics)
  * [Ref.](#ref)
<!-- TOC -->

___

## What Is a Class?
A class is a blueprint or prototype from which objects are created.

The following Bicycle class is one possible implementation of a bicycle:

```java
class Bicycle {

    int cadence = 0;
    int speed = 0;
    int gear = 1;

    void changeCadence(int newValue) {
         cadence = newValue;
    }

    void changeGear(int newValue) {
         gear = newValue;
    }

    void speedUp(int increment) {
         speed = speed + increment;   
    }

    void applyBrakes(int decrement) {
         speed = speed - decrement;
    }

    void printStates() {
         System.out.println("cadence:" +
             cadence + " speed:" + 
             speed + " gear:" + gear);
    }
}
```

The fields cadence, speed, and gear represent the object's state, and the methods (changeCadence, changeGear, speedUp etc.) define its interaction with the outside world.
[^3]

<sub>[Back to top](#table-of-contents)</sub>

## What Is an Object?
An object is a software bundle of related state and behavior. Software objects are often used to model the real-world objects that you find in everyday life.

Software objects are conceptually similar to real-world objects: they too consist of state and related behavior. An object stores its state in fields (variables in some programming languages) and exposes its behavior through methods (functions in some programming languages)

Methods operate on an object's internal state and serve as the primary mechanism for object-to-object communication. Hiding internal state and requiring all interaction to be performed through an object's methods is known as data encapsulation

![concepts-object.gif](../../../../img/concepts-object.gif)

To instance an object of the class Bicycle above, the syntax is:

```java
  Bicycle bike = new Bicycle();
```

First `Bicycle` establish the object type, the `=` operator assigns the object reference to be created to the variable `bike`, then `Bicycle()` stands for the calling of Bicycle constructor.


[^2]

<sub>[Back to top](#table-of-contents)</sub>

## What Is Inheritance?
Object-oriented programming allows classes to inherit commonly used state and behavior from other classes.

![concepts-bikeHierarchy.gif](../../../../img/concepts-bikeHierarchy.gif)

The syntax for creating a subclass is simple.

```java
class MountainBike extends Bicycle {

    // new fields and methods defining 
    // a mountain bike would go here

}
```

[^4]

<sub>[Back to top](#table-of-contents)</sub>

## Encapsulation and Data Hiding
Encapsulation and data hiding are fundamental concepts in Java that support the principle of information hiding and help in creating robust and maintainable code


### Encapsulation
Encapsulation is the process of bundling data (variables) and methods (functions) that operate on that data into a single unit called an object. It involves grouping related data and behaviors together to form a cohesive entity. Encapsulation helps in organizing code, improving code maintainability, and protecting data integrity.

In Java, encapsulation is achieved by declaring variables as private and providing public methods (getters and setters) to access and modify the data. This allows controlled access to the internal state of an object, ensuring that it can be manipulated only through well-defined interfaces.

### Data Hiding
Data hiding is a principle of encapsulation that emphasizes restricting direct access to the internal data of an object from outside code. It ensures that data can be accessed and modified only through the defined public methods, providing a layer of abstraction and preventing unauthorized modifications.

By hiding the internal data, Java enforces encapsulation, allowing objects to maintain their own internal state and behavior while controlling how external code interacts with them. This reduces dependencies, increases code flexibility, and protects data from accidental modifications or inconsistencies.

### Example

```java
public class Person {
private String name;
private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age >= 0) {
            this.age = age;
        } else {
            System.out.println("Invalid age. Age must be non-negative.");
        }
    }
}
```

In this example, the Person class encapsulates the data of a person's name and age. The name and age variables are declared as private, ensuring they are not directly accessible from outside the class.

To access and modify the data, the class provides public getter (getName, getAge) and setter (setName, setAge) methods. These methods allow controlled access to the private data, enforcing encapsulation and data hiding. The setter for age includes validation logic to ensure the age is non-negative.

```java
public class Main {
public static void main(String[] args) {
Person person = new Person();
person.setName("John");
person.setAge(25);

        System.out.println("Name: " + person.getName());
        System.out.println("Age: " + person.getAge());

        person.setAge(-5); // Trying to set an invalid age
    }
}
```

In the Main class, an instance of Person is created. The public setter and getter methods are used to set and retrieve the person's name and age. The encapsulated data is accessed and modified through these methods, ensuring controlled access and maintaining the integrity of the data.


<sub>[Back to top](#table-of-contents)</sub>


## What Is an Interface?
An interface is a contract between a class and the outside world. When a class implements an interface, it promises to provide the behavior published by that interface.

In its most common form, an interface is a group of related methods with empty bodies. A bicycle's behavior, if specified as an interface, might appear as follows:

```java
interface Bicycle {

    //  wheel revolutions per minute
    void changeCadence(int newValue);

    void changeGear(int newValue);

    void speedUp(int increment);

    void applyBrakes(int decrement);
}
```

To implement this interface, the name of your class would change (to a particular brand of bicycle, for example, such as ACMEBicycle), and you'd use the implements keyword in the class declaration:

```java
class ACMEBicycle implements Bicycle {

    int cadence = 0;
    int speed = 0;
    int gear = 1;

// The compiler will now require that methods
// changeCadence, changeGear, speedUp, and applyBrakes
// all be implemented. Compilation will fail if those
// methods are missing from this class.

    void changeCadence(int newValue) {
         cadence = newValue;
    }

    void changeGear(int newValue) {
         gear = newValue;
    }

    void speedUp(int increment) {
         speed = speed + increment;   
    }

    void applyBrakes(int decrement) {
         speed = speed - decrement;
    }

    void printStates() {
         System.out.println("cadence:" +
             cadence + " speed:" + 
             speed + " gear:" + gear);
    }
}
```

Implementing an interface allows a class to become more formal about the behavior it promises to provide.
[^5]

<sub>[Back to top](#table-of-contents)</sub>

## Polymorphism
Refers to the ability of objects of different classes to be treated as instances of a common superclass or interface. It allows for the use of a single interface to represent multiple types and the ability to provide different implementations based on the actual type of the object at runtime. 

In Java, polymorphism is primarily achieved through method overriding and method overloading.

### Method Overriding
Method overriding allows a subclass to provide a different implementation of a method that is already defined in its superclass. It involves creating a method in the subclass with the same name, return type, and parameters as the method in the superclass. The subclass method overrides the superclass method, and the appropriate method is invoked based on the actual type of the object at runtime.
Example:

```java
class Animal {
  public void makeSound() {
    System.out.println("Animal makes a sound");
  }
}

class Dog extends Animal {
  @Override
  public void makeSound() {
    System.out.println("Dog barks");
  }
}

class Cat extends Animal {
  @Override
  public void makeSound() {
    System.out.println("Cat meows");
  }
}

public class Main {
  public static void main(String[] args) {
    Animal animal1 = new Dog();
    Animal animal2 = new Cat();

    animal1.makeSound(); // Output: "Dog barks"
    animal2.makeSound(); // Output: "Cat meows"
  }
}
```

In this example, the Animal class has a makeSound method. The Dog and Cat classes extend the Animal class and provide their own implementation of the makeSound method. When objects of Dog and Cat are assigned to Animal references and the makeSound method is called, the appropriate method for the actual object type is invoked.

### Method Overloading

Method overloading allows multiple methods with the same name but different parameter types or a different number of parameters to coexist within a class. It enables the use of the same method name for different operations or variations of a task, providing convenience and flexibility in method invocation.

#### Example

```java
class Calculator {
  public int add(int num1, int num2) {
    return num1 + num2;
  }

  public double add(double num1, double num2) {
    return num1 + num2;
  }
}

public class Main {
  public static void main(String[] args) {
    Calculator calculator = new Calculator();

    int sum1 = calculator.add(2, 3); // Output: 5
    double sum2 = calculator.add(2.5, 3.7); // Output: 6.2
  }
}
```

In this example, the Calculator class has two add methods, one accepting two integers and the other accepting two doubles. The method overloading allows different types of parameters to be passed, and the appropriate method is selected based on the arguments passed at compile-time.

<sub>[Back to top](#table-of-contents)</sub>

## What Is a Package?
A package is a namespace for organizing classes and interfaces in a logical manner. Placing your code into packages makes large software projects easier to manage.

The Java platform provides an enormous class library (a set of packages) suitable for use in your own applications. This library is known as the "Application Programming Interface", or "API" for short. Its packages represent the tasks most commonly associated with general-purpose programming.

The [Java Platform API Specification](https://docs.oracle.com/javase/8/docs/api/index.html) contains the complete listing for all packages, interfaces, classes, fields, and methods supplied by the Java SE platform

<sub>[Back to top](#table-of-contents)</sub>

_____

## Q&A

Common questions a software architect trainee would ask about this topic.

**Q: In the Animal/Dog/Cat example, how does the JVM know which makeSound() to call at runtime?**
A: That's method overriding resolved via dynamic dispatch — even though the reference type is `Animal`, the JVM looks up the actual object's runtime type (`Dog` or `Cat`) and invokes its overridden method, which is what makes polymorphism work.

---

**Q: Why does the Person example use both `private` fields and public getters/setters instead of just making the fields public?**
A: That's encapsulation and data hiding in practice — keeping `name` and `age` private forces all access through methods, so `setAge()` can enforce the non-negative validation rule; public fields would let any caller bypass that invariant.

---

**Q: What's the practical difference between overriding and overloading shown on this page?**
A: Overriding (Dog/Cat extending Animal) redefines an inherited method with the same signature and is resolved at runtime based on the object's actual type; overloading (Calculator's `add(int,int)` vs `add(double,double)`) defines multiple methods with the same name but different parameters and is resolved at compile time based on argument types.

<sub>[Back to top](#table-of-contents)</sub>

---

## Related Topics

- [Object-Oriented Programming](../../paradigms/object-oriented.md) — the language-agnostic paradigm these Java mechanics implement.
- [SOLID Principles](../../../design-patterns/solid.md) — design principles that build directly on encapsulation, abstraction, and polymorphism.
- [Generics](java-5/generics.md) — adds compile-time type safety on top of the class/interface model described here.
- [Java](java.md) — language overview linking back to this page's OOP concepts.

<sub>[Back to top](#table-of-contents)</sub>

---

## Ref.

- https://docs.oracle.com/javase/tutorial/java/concepts/index.html

[^2]: https://docs.oracle.com/javase/tutorial/java/concepts/object.html
[^3]: https://docs.oracle.com/javase/tutorial/java/concepts/class.html
[^4]: https://docs.oracle.com/javase/tutorial/java/concepts/inheritance.html
[^5]: https://docs.oracle.com/javase/tutorial/java/concepts/interface.html

___

[Get Started](../../../../get-started.md) |
[Paradigms](../../../../get-started.md#paradigms) |
[OOP](../../paradigms/object-oriented.md) |
[Java](java.md#whats-oop)

___