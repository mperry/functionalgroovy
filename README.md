Functional Groovy
=================

[Functional Groovy](https://github.com/mperry/functionalgroovy) is a library for doing functional programming
(FP) in Groovy.  It is a Groovy extension module for [Functional Java](http://functionaljava.org/) (FJ), adding Groovy idioms and new FP constructs in Groovy.

Features includes:
* FunctionalJava based
* Enhances FunctionalJava for Groovy as a Groovy extension module
* Groovy quickcheck style testing
* Monadic functions added to the standard Java List
* Monad comprehensions (dynamically typed)
* Monad library using a minimal monad implementation (unit/flatMap)
* Lenses
* Reader, Writer and State monads
* A simple IO type

It is divided into four components: core, main, demo and consume.
* Core enhances Functional Java with Groovy idioms
* Main adds new functionality
* Demo includes examples of FP in Groovy and usage of this library
* Consume shows how to include FunctionalGroovy in your project

I have written an initial [blog post](http://mperry.github.io/2013/07/28/groovy-null-handling.html) covering:
* some introductory material on functional programming in Groovy
* how to begin using the Functional Groovy library
* handling `null`s by binding through the `Option` type, monadic comprehensions and monadic lifting

To start using the library add the dependency `com.github.mperry:functionalgroovy-main:0.3-SNAPSHOT` to your Gradle
project. A simple test script to get going (`test.groovy`) is:

```groovy
@GrabResolver('https://oss.sonatype.org/content/groups/public')
@Grab('com.github.mperry:functionalgroovy-core:0.3-SNAPSHOT')
@Grab('org.functionaljava:functionaljava:3.1')

import com.github.mperry.fg.*

1.to(5).each {
    println it
}
```

Run this script using `groovy test.groovy`.

This project uses:
* JDK 7
* Gradle 1.9
* Groovy 2.1.6

I have added a list of [open issues](https://github.com/mperry/functionalgroovy/issues?state=open) so feel free to
contribute.  Some ways of contributing are:
* adding new functionality
* adding tests
* adding FP in Groovy examples
* adding FunctionalGroovy usage examples
