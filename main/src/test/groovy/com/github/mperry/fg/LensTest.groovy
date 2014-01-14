package com.github.mperry.fg

import fj.F
import fj.F2
import groovy.transform.Canonical
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Before
import org.junit.Test

import static com.github.mperry.fg.Comprehension.foreach
import static junit.framework.Assert.assertTrue

/**
 * Created by MarkPerry on 14/01/14.
 */
@TypeChecked
class LensTest {

    @Canonical
    class Person {
        String name
        Integer age
        Address address
    }

    @Canonical
    class Address {
        Integer number
        String street
    }

    def nameLens = Lens.lift(
            { Person p -> p.name } as F,
            { Person p, String name -> new Person(name, p.age, p.address) } as F2
    )

    def ageLens = new Lens<Person, Integer>(
            { Person p -> p.age } as F,
            { Person p, Integer i -> new Person(p.name, i, p.address) } as F2
    )

    def addressLens = new Lens<Person, Address>(
            { Person p -> p.address } as F,
            { Person p, Address a -> new Person(p.name, p.age, a) } as F2
    )
    def streetLens = new Lens<Address, String>(
            { Address a -> a.street } as F,
            { Address a, String s -> new Address(a.number, s) } as F2
    )
    String oldName = "joe"
    Integer oldAge = 25
    Integer newAge = 30
    String oldStreet = "Hill"
    String newStreet = "Mountain"
    Integer streetNumber = 10

    Address address = new Address(streetNumber, oldStreet)
    Person person = new Person(oldName, oldAge, address)

    @Before
    void setUp() {
//        address = new Address(streetNumber, oldStreet)
//        person = new Person(oldName, oldAge, address)
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void simple() {
        def p2 = ageLens.set(person, newAge)
        assertTrue(ageLens.get(person) == oldAge)
        assertTrue(person.name == oldName && person.age == oldAge && p2.name == oldName && p2.age == newAge)
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void mod() {
        def p2 = ageLens.mod(person, { Integer i -> i + 1} as F)
        assertTrue(person.age == oldAge && p2.name == oldName && p2.age == oldAge + 1)
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void compose() {
        def personStreetLens = streetLens.compose(addressLens)
        def p1 = personStreetLens.set(person, newStreet)
        assertTrue(personStreetLens.get(person) == oldStreet)
        assertTrue(personStreetLens.get(p1) == newStreet)
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void test1() {
        def add = 2
        def addSurname = " jones"
        def state = foreach {
            age << ageLens.mod({ i -> i + 2 } as F)
            name << nameLens.state()
            newName << { this.nameLens.update(name + addSurname) }
            p << StateM.get()
            yield { p }
        }
        def p2 = state.eval(person)
        println "person $p2"
        assertTrue(p2.age == oldAge + add && p2.name == oldName + addSurname && p2.address == address)
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void test2() {
        foreach {
            a << { 1.to(3) }
            b << { a.to(10) }
//            c << { println "$a $b" }
            yield { 2 }

        }
    }

}
