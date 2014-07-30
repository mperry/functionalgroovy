package com.github.mperry.fg

import com.github.mperry.fg.typeclass.concrete.OptionMonad
import fj.F
import fj.F2
import fj.P
import fj.P2
import fj.data.Option
import fj.function.Integers
import groovy.transform.TypeChecked
import org.junit.Test

import static fj.Function.*
import static fj.data.Option.*
import static junit.framework.Assert.assertTrue

/**
 * Created by MarkPerry on 13/04/2014.
 */
@TypeChecked
class OptionMonadExtensionTest {

    OptionMonad monad() {
        new OptionMonad()
    }

    @Test
    void div() {
        def base = 32
        def input = [P.p(2, 2), P.p(0, 2)]
        def result = input.collect { P2<Integer, Integer> p ->
            def o1 = monad().flatMap(some(base), { Integer x -> safeDiv(x, (Integer) p._1())})
            def o2 = monad().flatMap(o1, { Integer y -> safeDiv(y, (Integer) p._2())})
            o2
        }
        println result
        def expected = [some(8), none()]
        assertTrue(result == expected)
    }

    static Option<Integer> safeDiv(Integer num, Integer denom) {
        denom == 0 ? Option.<Integer>none() : some(num.intdiv(denom).intValue())
    }

    @Test
    void ap() {
        def f = { Integer i -> i + 1} as F
        assertTrue(monad().ap(some(1), some(f)) == some(2))
        assertTrue(monad().ap(some(1), none()) == none())
        assertTrue(monad().ap(none(), some(f)) == none())
    }

    @Test
    void filterM() {
        def f = { Integer i -> some(i > 0)} as F
        def actual = monad().filterM([2, 1, 0, -1], f)
        assertTrue(actual == some([2, 1]))
    }

    @Test
    void fmap() {
        def actual = monad().map(some(1), Integers.negate)
        assertTrue(actual == some(-1))
    }

    @Test
    void foldM() {
        def saveDiv = { BigDecimal n, Integer d -> d == 0 ? none() : some(n/d) } as F2
        assertTrue(monad().foldM([4, 5], 40.toBigDecimal(), saveDiv) == some(2.toBigDecimal()))
        assertTrue(monad().foldM([4, 0, 2], 40.toBigDecimal(), saveDiv) == none())
    }

    @Test
    void join() {
        assertTrue(monad().join(some(some(1))) == some(1))
        assertTrue(monad().join(some(none())) == none())
        assertTrue(monad().join(none()) == none())
    }

    @Test
    void liftM() {
        def f = Integers.negate
        assertTrue(monad().liftM(some(4), f) == some(-4))
        assertTrue(monad().liftM(none(), f) == none())
    }

    @Test
    void liftM2() {
        def f = uncurryF2(Integers.multiply)
        assertTrue(monad().liftM2(some(3), some(4), f) == some(12))
        assertTrue(monad().liftM2(some(3), none(), f) == none())
        assertTrue(monad().liftM2(none(), none(), f) == none())
    }

    @Test
    void sequence() {
//        println actual
        assertTrue(monad().sequence([some(2), some(3)]) == some([2, 3]))
        assertTrue(monad().sequence([some(2), none()]) == none())
    }

    @Test
    void replicateM() {
        assertTrue(monad().replicateM(4, some(3)) == some([3, 3, 3, 3]))
        assertTrue(monad().replicateM(3, none()) == none())
    }

    @Test
    void traverse() {
        def f = { Integer i -> i > 0 ? some(i) : none() } as F
        assertTrue(monad().traverse([2, -4, 6], f) == none())
        assertTrue(monad().traverse([2, 6], f) == some([2, 6]))
    }

}
