package com.github.mperry.fg

import fj.F
import fj.F2
import groovy.transform.Canonical
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Before
import org.junit.Test

import static com.github.mperry.fg.Comprehension.foreach
import static com.github.mperry.fg.StateM.get
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

    Lens<Person, String> nameLens = Lens.lift(
            { Person p -> p.name } as F,
            { Person p, String name -> new Person(name, p.age, p.address) } as F2
    )

    Lens<Person, Integer> ageLens = new Lens<Person, Integer>(
            { Person p -> p.age } as F,
            { Person p, Integer i -> new Person(p.name, i, p.address) } as F2
    )

    Lens<Person, Address> addressLens = new Lens<Person, Address>(
            { Person p -> p.address } as F,
            { Person p, Address a -> new Person(p.name, p.age, a) } as F2
    )
    Lens<Address, String> streetLens = new Lens<Address, String>(
            { Address a -> a.street } as F,
            { Address a, String s -> new Address(a.number, s) } as F2
    )

    String oldName = "Joe"
    Integer oldAge = 25
    Integer newAge = 30
    String oldStreet = "Hill"
    String newStreet = "Mountain"
    Integer oldStreetNumber = 10

    Address address = new Address(oldStreetNumber, oldStreet)
    Person person = new Person(oldName, oldAge, address)

    @Before
    void setUp() {
        
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
        def addSurname = " Smith"
        def streetMod = "Green "
        def street = addressLens.andThen(streetLens)
        def name = nameLens
        def age = ageLens

        StateM state = foreach {
            age1 << age.mod { it + 2 }
            street1 << street.state()
            street2 << { street.update(streetMod + street1) }
            name1 << name.state()
            newName << { name.update(name1 + addSurname) }
            p << get()
            yield { p }
        }
        def p2 = state.eval(person)
        println "person $p2"
        assertTrue(p2.age == oldAge + add && p2.name == oldName + addSurname &&
                p2.address == new Address(oldStreetNumber, streetMod + oldStreet))
    }

    Person eval(StateM<Person, Person> state) {
        (Person) state.eval(person)
    }

    StateM<Person, Person> createState(Integer add, String addSurname, String streetMod) {
        def street = addressLens.andThen(streetLens)
        def state = ageLens.mod { Integer it -> it + 2 }.flatMap { Integer age1 ->
            street.state().flatMap { String street1 ->
                street.update(streetMod + street1).flatMap { String street2 ->
                    nameLens.state().flatMap { String name1 ->
                        nameLens.update(name1 + addSurname).flatMap { String newName ->
                            StateM.<Person>get().map { Person p ->
                                p
                            }
                        }
                    }
                }
            }
        }
        state
    }

    @Test
//    @TypeChecked(TypeCheckingMode.SKIP)
    void test2() {
        def add = 2
        def addSurname = " Smith"
        def streetMod = "Green "

        def state = createState(add, addSurname, streetMod)
        def p2 = eval(state)
        println "person $p2"
        assertTrue(p2.age == oldAge + add && p2.name == oldName + addSurname &&
                p2.address == new Address(oldStreetNumber, streetMod + oldStreet))

    }

}
