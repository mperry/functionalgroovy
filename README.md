Functional Groovy
===========

Functional Groovy (https://github.com/mperry/functionalgroovy) is a Groovy extension module for Functional Java,
enhancing Functional Java for Groovy idioms.

It is divided into two components, core and main.  Core enhances Functional Java with Groovy idioms whilst main
adds further functionality.  At the moment main just includes Monadic comprehensions (e.g. supports Scala and
Haskell like for comprehensions).

I have written an initial blog post (http://mperry.github.io/2013/07/28/groovy-null-handling.html) covering:
- some introductory material on functional programming in Groovy
- how to begin using the Functional Groovy library
- handling nulls by binding through the Option type, monadic comprehensions and monadic lifting

To start using the library add the dependency 'com.github.mperry:functionalgroovy-main:0.2-SNAPSHOT' to your Gradle
project.  A simple test script to get going (test.groovy) is:

///////////////////
@GrabResolver('https://oss.sonatype.org/content/groups/public')
@Grab('com.github.mperry:functionalgroovy-core:0.2-SNAPSHOT')
@Grab('org.functionaljava:functionaljava:3.1')

import com.github.mperry.fg.*

1.to(5).each {
    println it
}
///////////////////

Run this script using "groovy test.groovy".

This project uses
- JDK 7
- Gradle 1.6
