package com.github.mperry.fg.typeclass.trait

import com.github.mperry.fg.typeclass.concrete.OptionMonad
import fj.F
import fj.F2
import fj.function.Integers
import groovy.transform.TypeChecked
import org.junit.Test

import static fj.Function.uncurryF2
import static fj.data.Option.none
import static fj.data.Option.some
import static junit.framework.Assert.assertTrue

/**
 * Created by MarkPerry on 13/04/2014.
 */
@TypeChecked
class OptionMonadTExtensionTest {

    OptionMonad monad() {
        new OptionMonad()
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
        def actual = monad().fmap(Integers.negate, some(1))
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
