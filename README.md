Functional Groovy
=================

[Functional Groovy](https://github.com/mperry/functionalgroovy) is a library for doing functional programming
(FP) in Groovy.  It is a Groovy extension module for [Functional Java](http://functionaljava.org/) (FJ), adding Groovy idioms and new FP constructs in Groovy.

Features includes:
* FunctionalJava based
* Enhances FunctionalJava for Groovy as a Groovy extension module
* Groovy Quickcheck style property testing (specification based testing)
* Monad library using a minimal monad implementation (unit/flatMap)
* Monadic functions added to the standard Java List
* Monad comprehensions (dynamically typed)
* Lenses
* Reader, Writer and State monads
* A simple IO type
* Y Combinator

I have written an initial blog post on [Groovy Null Handling using Bind, Comprehensions and Lift](http://mperry.github.io/2013/07/28/groovy-null-handling.html) covering:
* some introductory material on functional programming in Groovy
* how to begin using the Functional Groovy library
* handling `null`s by binding through the `Option` type, monadic comprehensions and monadic lifting

The full list of related posts are:
* "Groovy Null Handling Using Bind, Comprehensions and Lift":http://mperry.github.io/2013/07/28/groovy-null-handling.html
* "Specification Based Testing":http://mperry.github.io/2013/12/09/specification-based-testing.html
* "Referentially Transparent Input/Output in Groovy":http://mperry.github.io/2014/01/03/referentially-transparent-io.html
* "Folds and Unfolds":http://mperry.github.io/2014/01/21/folds-and-unfolds.html

To start using the library add the dependency `com.github.mperry:functionalgroovy-main:0.4-SNAPSHOT` to your Gradle
project. A simple test script to get going (`test.groovy`) is:

```groovy
@GrabResolver('https://oss.sonatype.org/content/groups/public')
@Grab('com.github.mperry:functionalgroovy-core:0.4-SNAPSHOT')
@Grab('org.functionaljava:functionaljava:3.1')

import com.github.mperry.fg.*

1.to(5).each {
    println it
}
```

Run this script using `groovy test.groovy`.

This project uses:
* JDK 7
* Gradle 1.10
* Groovy 2.1.6
* Intellij Community Edition 13.0.2

I have added a list of [open issues](https://github.com/mperry/functionalgroovy/issues?state=open) so feel free to
contribute.  Some ways of contributing are:
* adding new functionality
* adding tests
* adding FP in Groovy examples
* adding FunctionalGroovy usage examples

Functional Groovy is divided into four components: core, main, demo and consume.
* Core enhances Functional Java with Groovy idioms
* Main adds new functionality
* Demo includes examples of FP in Groovy and usage of this library
* Consume shows how to include FunctionalGroovy in your project
