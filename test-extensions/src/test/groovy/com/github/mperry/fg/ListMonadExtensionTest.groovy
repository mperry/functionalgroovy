package com.github.mperry.fg

import com.github.mperry.fg.typeclass.Monad
import com.github.mperry.fg.typeclass.concrete.ListMonad
import com.github.mperry.fg.typeclass.trait.*
import fj.F
import fj.F2
import fj.F3
import fj.Unit
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

import static fj.Unit.unit
import static junit.framework.Assert.assertTrue

/**
 * Created by MarkPerry on 16/01/14.
 */
@TypeChecked
class ListMonadExtensionTest {

    ListMonad monad() {
        new ListMonad()
    }

    @Test
    void join() {
        def source = [[1, 2], [3, 4], []]
        def expected = [1, 2, 3, 4]
        assertTrue(List.join(source) == expected)
        assertTrue(monad().join(source) == expected)
    }

    @Test
    void map() {
        def f = { Integer i -> i * 2} as F
        def source = [1, 2, 3]
        def expected = [2, 4, 6]
        assertTrue(source.map(f) == expected)
        assertTrue(monad().map(source, f) == expected)
    }

    @Test
    void map2() {
        def f = { Integer i, Integer j -> i * j } as F2
        def list1 = [1, 2]
        def list2 = [3, 4]
        def expected = [3, 4, 6, 8]
        assertTrue(list1.map2(list2, f) == expected)
        assertTrue(monad().map2(list1, list2, f) == expected)
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void to() {
        def s = "a"
        def source = [1, 2]
        def expected = [s, s]
        assertTrue(source.to(s) == expected)
        assertTrue(monad().to(source, s) == expected)
    }

    @Test
    void skip() {
        def expected = [unit(), unit()]
        def source = [1, 2]
        assertTrue(source.skip() == expected)
        assertTrue(monad().skip(source) == expected)
    }

    @Test
    void foldM() {
        def f = { Integer acc, Integer i -> [acc + i] } as F2
        def source = 1.to(3)
        def init = 0
        def list = List.foldM(source, init, f)
        def expected = [6]
        assertTrue(list == expected)
        assertTrue(monad().foldM(source, init, f) == expected)
    }

    @Test
    void foldM_() {
        def f = { Integer acc, Integer i -> [acc + i] } as F2
        def source = 1.to(3)
        def init = 0
        def list = List.foldM_(source, init, f)
        def expected = [unit()]
        assertTrue(list == expected)
        assertTrue(monad().foldM_(source, init, f) == expected)
    }

    @Test
    void sequence() {
        def source = [[1, 2], [3, 4, 5]]
        def list = List.sequence(source)
//        println list
        def expected = [[1,3],[1,4],[1,5],[2,3],[2,4],[2,5]]
        assertTrue(list == expected)
        assertTrue(monad().sequence(source) == expected)
    }

    @Test
    void traverse() {
        def f = { Integer i -> 1.to(i).toJList()} as F
        def source = [1, 2, 3]
//        println list
        def expected = [[1,1,1],[1,1,2],[1,1,3],[1,2,1],[1,2,2],[1,2,3]]
        assertTrue(source.traverse(f) == expected)
        assertTrue(monad().traverse(source, f) == expected)
    }

    @Test
    void replicateM() {
        def source = [1, 2]
        def n = 3
        def list = source.replicateM(n)
//        println list
        def expected = [[1,1,1],[1,1,2],[1,2,1],[1,2,2],[2,1,1],[2,1,2],[2,2,1],[2,2,2]]
        assertTrue(list == expected)
        assertTrue(monad().replicateM(n, source) == expected)
    }

    @Test
    void compose() {
        def f = { String s -> [s, s] } as F
        def g = { Integer i ->
            1.to(i * 2).toJavaList().map({ Integer j ->
                j.toString()
            } as F)
        } as F
        def func = List.compose(f, g)
        def v = 3
        def expected = [1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6].collect { it.toString() }
        assertTrue(List.compose(f, g).f(v) == expected)
        assertTrue(monad().compose(f, g).f(v) == expected)
    }

    @Test
    void filterM() {
        def f = { Integer i ->
            [i > 0]
        } as F
        def source = [2, 1, 0, -1]
//        println actual
        def expected = [[2, 1]]
        assertTrue(List.filterM(source, f) == expected)
        assertTrue(monad().filterM(source, f) == expected)
    }

    @Test
    void when() {
        // TODO: not sure if this is applicable
    }

    @Test
    void unless() {
        // TODO: not sure if this is applicable
    }

    @Test
    void liftM() {
        def source = [1, 2, 3]
        def f = { Integer i -> i * 2 } as F
        def expected = source.map(f)
//        println actual
        assertTrue(source.liftM(f) == expected)
        assertTrue(monad().liftM(source, f) == expected)
    }

    @Test
    void liftM2() {
        def f = { Integer i, Integer j -> i + j } as F2
        def list1 = [0, 1]
        def list2 = [0, 2]
        def expected = [0, 2, 1, 3]
//        println actual
        assertTrue(list1.liftM2(list2, f) == expected)
        assertTrue(monad().liftM2(list1, list2, f) == expected)
    }

    @Test
    void liftM3() {
        // TODO: continue from here
        def f = {Integer i, Integer j, Integer k -> i + j + k } as F3
        def list1 = [0, 1]
        def list2 = [0, 2]
        def list3 = [0, 4]
        def expected = [0, 4, 2, 6, 1, 5, 3, 7]
        assertTrue(list1.liftM3(list2, list3, f) == expected)
        assertTrue(monad().liftM3(list1, list2, list3, f) == expected)
    }

    @Test
    void ap() {
        // Haskell:
        // Prelude Control.Monad> ap (map (\x -> (\i -> i * (x + 2))) [3..5]) [1..4]
        def fs = 3.to(5).map { Integer i ->
            def f = { Integer j -> j * (i + 2)}
            f as F
        }.toJavaList()
        def list = 1.to(4).toJavaList()
        def expected = [5,10,15,20,6,12,18,24,7,14,21,28]
//        println actual
        assertTrue(list.ap(fs) == expected)
        assertTrue(monad().ap(list, fs) == expected)
    }

    @Test
    void apply() {
        // same implementation as ap
    }

}
