package com.github.mperry.fg.typeclass

import com.github.mperry.fg.typeclass.concrete.OptionApplicative
import fj.F
import fj.F3
import fj.F3Functions
import fj.Function
import groovy.transform.TypeChecked
import org.junit.Assert
import org.junit.Test

import static fj.data.Option.none
import static fj.data.Option.some
import static org.junit.Assert.assertTrue

/**
 * Created by mperry on 2/07/2014.
 */
@TypeChecked
class OptionApplicativeTest {

    @Test
    void test1() {
        def app = new OptionApplicative()
        F<Integer, Integer> f = { Integer a -> 3 + a } as F
        def o1 = app.apply(some(f), some(10))
        def o2 = app.apply(some({ Integer a -> 3 + a } as F), some(10))
        def o3 = app.apply(some(f), none())

        // use the discriminate for quadratic equations: b^2 - 4ac
        F3<Integer, Integer, Integer, Integer> f3 = { Integer a, Integer b, Integer c -> b * b - 4 * a * c } as F3
        def o4 = app.apply(app.apply(app.apply(app.pure(Function.curry(f3)), some(4)), some(5)), some(3))
        // note, with infix methods we could have written:
        // app.pure(Function.curry(f3)) app.apply some(4) app.apply some(5) app.apply some(3)

        def o5 = app.liftA3(f3, some(4), some(5), some(3))

        println o1
        println o2
        println o3
        println o4
        println o5


    }

    @Test
    void testSeq() {
        def app = new OptionApplicative()
        def s1 = app.sequenceA([some(3), none(), some(1)])
        assertTrue(s1 == none())
        def s2 = app.sequenceA([some(3), some(2), some(1)])
        assertTrue(s2 == some([3, 2, 1]))
//        println s1
//        println s2

    }

}
