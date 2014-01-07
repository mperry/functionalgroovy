package com.github.mperry.fg

import fj.F
import fj.F2
import fj.F3
import fj.data.Option
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

import static fj.Function.compose
import static fj.test.Arbitrary.arbF
import static fj.test.Arbitrary.arbF
import static fj.test.Arbitrary.arbInteger
import static fj.test.Arbitrary.arbList
import static fj.test.Arbitrary.arbLong
import static fj.test.Arbitrary.arbOption
import static fj.test.Arbitrary.arbString
import static fj.test.Coarbitrary.coarbInteger
import static fj.test.Coarbitrary.coarbLong
import static fj.test.Property.prop
import static fj.test.Property.property
import static org.junit.Assert.assertTrue

/**
 * Created by MarkPerry on 30/12/13.
 */
@TypeChecked
class OptionMonadTest {

    OptionMonad monad() {
        new OptionMonad()
    }

    @Test
    void testUnit() {
        def m = monad()
        def o1 = m.unit(3)
        assertTrue(o1.some() == 3)
    }

    @Test
    void testFlatMap() {
        def m = monad()
        def o1 = m.unit(3)
        def o2 = m.flatMap(o1, {Integer i -> Option.some(2 * i)} as F)
        assertTrue(o2.some() == 6)
    }

    @Test
    void testMap() {
        def m = monad()
        def o2 = m.map(m.unit(3), { Integer i -> (2 * i).toString()} as F)
        assertTrue(o2.some() == 6.toString())
    }

    // now encode the monad laws

    // Left identity: return a >>= f == f a
    @Test
    void leftIdentity() {
        def p = property(arbF(coarbInteger, arbOption(arbString)), arbInteger, {
            F<Integer, Option<String>> f, Integer a ->
                def m = monad()
                def b = m.flatMap(m.unit(a), f).equals(f.f(a))
                prop(b)
        } as F2)
        p.checkOkWithSummary()
    }

    // Right identity: m >>= return == m
    @Test
    void rightIdentity() {
        def p = property(arbOption(arbInteger), {
            Option<Integer> o ->
                def m = monad()
                prop(m.flatMap(o, m.unit()).equals(o))
        } as F)
        p.checkOkWithSummary()
    }

    // Associativity: (m >>= f) >>= g == m >>= (\x -> f x >>= g)
    @Test
    void associativity() {
        def p = property(arbOption(arbInteger), arbF(coarbInteger, arbOption(arbLong)), arbF(coarbLong,
                arbOption(arbString)), {
            Option<Integer> o, F<Integer, Option<Long>> f, F<Long, Option<String>> g ->
                def m = monad()
                prop(m.flatMap(m.flatMap(o, f), g).equals(m.flatMap(o, { Integer i -> m.flatMap(f.f(i), g)} as F)))
        } as F3)
        p.checkOkWithSummary()
    }

}
