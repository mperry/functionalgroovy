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
class SetMonadExtensionTest2 {

    SetMonad monad() {
        new SetMonad()
    }

    @Test
    void ap() {
        def f = { Integer i -> i + 1} as F
        def source = [1, 1, 2].toSet()
        def fs = [f, f].toSet()
        def expected = [2, 3].toSet()
        assertTrue(source.ap(fs) == expected)
        assertTrue(monad().ap(source, fs) == expected)
    }

    @Test
    void filterM() {
        def f = { Integer i -> [i > 0].toSet()} as F
        def source = [-1, 2, 0, 1, -1]
        def actual = monad().filterM(source, f)
//        println actual
        def expected = [[2, 1]].toSet()
        assertTrue(monad().filterM(source, f) == expected)
        assertTrue(Set.filterM(source, f) == expected)
    }

    @Test
    void fmap() {
        def f = Integers.negate
        def source = [2, 3].toSet()
        def expected = [-2, -3].toSet()
        assertTrue(monad().fmap(f, source) == expected)
        assertTrue(source.fmap(f) == expected)
    }

    @Test
    void foldM() {
        def f = { Integer i, Integer j -> [i + j].toSet() } as F2
        def init = 1
        def source = [2, 4, 5]
//        println actual
        def expected = [12].toSet()
        assertTrue(monad().foldM(source, init, f) == expected)
        assertTrue(Set.foldM(source, init, f) == expected)

    }

    @Test
    void join() {
        def s1 = [[2, 4].toSet(), [2, 6].toSet()].toSet()
//        println actual
        def expected = [2, 4, 6].toSet()
        assertTrue(monad().join(s1) == expected)
        assertTrue(Set.join(s1) == expected)
    }

    @Test
    void liftM() {
        def f = Integers.negate
        def expected = [-2, -4].toSet()
        def source = [2, 4].toSet()
        assertTrue(monad().liftM(source, f) == expected)
        assertTrue(source.liftM(f) == expected)
    }

    @Test
    void liftM2() {
        def f = uncurryF2(Integers.multiply)
        def expected = [8, 10, 12, 15].toSet()
        def s1 = [2, 3].toSet()
        def s2 = [4, 5].toSet()
        assertTrue(monad().liftM2(s1, s2, f) == expected)
        assertTrue(s1.liftM2(s2, f) == expected)
    }

    @Test
    void sequence() {
        def source = [[1, 2].toSet(), [1, 3].toSet()]
//        println actual
        def expected = [[1, 1], [1, 3], [2, 1], [2, 3]].toSet()
        assertTrue(monad().sequence(source) == expected)
        assertTrue(Set.sequence(source) == expected)
    }

    @Test
    void replicateM() {
        def n = 3
        def s = [1, 2].toSet()
//        println actual
        def expected = [[1, 1, 1], [2, 2, 1], [1, 2, 2], [2, 1, 1], [1, 1, 2], [2, 2, 2], [2, 1, 2], [1, 2, 1]].toSet()
        assertTrue(monad().replicateM(n, s) == expected)
        assertTrue(s.replicateM(n) == expected)
    }

    @Test
    void traverse() {
        def f = { Integer i -> (i..i + 1).toSet() } as F
        def s = [2, 4]
//        println actual
        def expected = [[3, 4], [2, 4], [3, 5], [2, 5]].toSet()
        assertTrue(monad().traverse(s, f) == expected)
        assertTrue(Set.traverse(s, f) == expected)
    }

}
