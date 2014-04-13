package com.github.mperry.fg

import com.github.mperry.fg.typeclass.concrete.OptionMonad
import com.github.mperry.fg.typeclass.concrete.SetMonad
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
class SetMonadExtensionTest {

    SetMonad monad() {
        new SetMonad()
    }

    @Test
    void ap() {
        def f = { Integer i -> i + 1} as F
        assertTrue(monad().ap([1, 1, 2].toSet(), [f, f].toSet()) == [2, 3].toSet())
    }

    @Test
    void filterM() {
        def f = { Integer i -> [i > 0].toSet()} as F
        def actual = monad().filterM([-1, 2, 0, 1, -1], f)
//        println actual
        assertTrue(actual == [[2, 1]].toSet())
    }

    @Test
    void fmap() {
        def actual = monad().fmap(Integers.negate, [2, 3].toSet())
        assertTrue(actual == [-2, -3].toSet())
    }

    @Test
    void foldM() {
        def f = { Integer i, Integer j -> [i + j].toSet() } as F2
        def actual = monad().foldM([2, 4, 5], 1, f)
//        println actual
        assertTrue(actual == [12].toSet())

    }

    @Test
    void join() {
        def actual = monad().join([[2, 4].toSet(), [2, 6].toSet()].toSet())
//        println actual
        assertTrue(actual == [2, 4, 6].toSet())
    }

    @Test
    void liftM() {
        def f = Integers.negate
        assertTrue(monad().liftM([2, 4].toSet(), f) == [-2, -4].toSet())

    }

    @Test
    void liftM2() {
        def f = uncurryF2(Integers.multiply)
        assertTrue(monad().liftM2([2, 3].toSet(), [4, 5].toSet(), f) == [8, 10, 12, 15].toSet())
    }

    @Test
    void sequence() {
        def actual = monad().sequence([[1, 2].toSet(), [1, 3].toSet()])
//        println actual
        assertTrue(actual == [[1, 1], [1, 3], [2, 1], [2, 3]].toSet())
    }

    @Test
    void replicateM() {
        def actual = monad().replicateM(3, [1, 2].toSet())
//        println actual
        def expected = [[1, 1, 1], [2, 2, 1], [1, 2, 2], [2, 1, 1], [1, 1, 2], [2, 2, 2], [2, 1, 2], [1, 2, 1]].toSet()
        assertTrue(actual == expected)
    }

    @Test
    void traverse() {
        def f = { Integer i -> (i..i + 1).toSet() } as F
        def actual = monad().traverse([2, 4], f)
//        println actual
        def expected = [[3, 4], [2, 4], [3, 5], [2, 5]].toSet()
        assertTrue(actual == expected)
    }

}
