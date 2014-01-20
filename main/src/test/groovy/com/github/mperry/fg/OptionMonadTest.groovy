package com.github.mperry.fg

import fj.F
import fj.F2
import fj.F3
import fj.P1
import fj.data.Option
import fj.test.Arbitrary
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

import static fj.Function.compose
import static fj.data.Option.*
import static fj.test.Arbitrary.arbF
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
        def f = { Integer i -> some(2 * i) } as F
        def o2 = m.flatMap(o1, f)
        assertTrue(o2.some() == 6)
        assertTrue(m.flatMap(o1, f) == o1.flatMap(f))
    }

    @Test
    void testMap() {
        def m = monad()
        def f = { Integer i -> (2 * i).toString() } as F
        def o1 = m.unit(3)
        def o2 = m.map(o1, f)
        assertTrue(o2.some() == 6.toString())
        assertTrue(m.map(o1, f) == o1.map(f))
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

    @Test
    void abstractLeftIdentity() {
        new MonadLaws().leftIdentity(monad(), arbF(coarbInteger, arbOption(arbString)), arbInteger)
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

    @Test
    void abstractRightIdentity() {
        new MonadLaws().rightIdentity(monad(), arbOption(arbInteger))
    }

    // Associativity: (m >>= f) >>= g == m >>= (\x -> f x >>= g)
    @Test
    void associativity() {
        def p = property(arbOption(arbInteger), arbF(coarbInteger, arbOption(arbLong)), arbF(coarbLong,
                arbOption(arbString)), {
            Option<Integer> o, F<Integer, Option<Long>> f, F<Long, Option<String>> g ->
                def m = monad()
                prop(m.flatMap(m.flatMap(o, f), g).equals(m.flatMap(o, { Integer i -> m.flatMap(f.f(i), g) } as F)))
        } as F3)
        p.checkOkWithSummary()
    }

    @Test
    void abstractAssociativity() {
        new MonadLaws().associativity(monad(), arbOption(arbInteger), arbF(coarbInteger, arbOption(arbLong)),
                arbF(coarbLong, arbOption(arbString)))
    }

    /**
     * join has type signature: def <A> M<A> join(M<M<A>> mma), thus this call is not type safe,
     * despite the Groovy type checker saying it is ok
     */
    @Test
    void joinNotTypeSafe() {
        def m = monad()
        assertTrue(({ m.join(some(3)) } as P1).throwsException(ClassCastException.class))
    }

    @Test
    void join() {
        def m = monad()
        assertTrue(m.join(some(some(3))) == some(3))
        assertTrue(m.join(none()) == none())
        assertTrue(m.join(some(none())) == none())

        def s = some(some(3))
        assertTrue(m.join(s) == Option.join(s))
    }

    @Test
    void sequence() {
        def m = monad()
        assertTrue(m.sequence(Arrays.asList(some(3), none())) == none())
        assertTrue(m.sequence(Arrays.asList(some(3), some(4))) == some([3, 4]))
        assertTrue(m.sequence([some(3), some(4)]) == some([3, 4]))

        def list = [some(3), some(4)]
        assertTrue(m.sequence(list) == Option.sequence(list.toFJList()).map { fj.data.List l -> l.toJavaList() } )
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void ap1() {
        def m = monad()
        def of = some({ i -> { j -> i + j } as F } as F)
        def m1 = m.ap(some(2), of)
        def m2 = m.ap(some(3), m1)
//        println m2
        def b = m2 == some(5)
        assertTrue(m2 == some(5))
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void ap2() {
        def f = { i ->
            i + 1
        } as F
        def m = monad()
        def m1 = m.ap(some(1), some(f))
//        println m1
        assertTrue(m1 == some(2))
    }

}
