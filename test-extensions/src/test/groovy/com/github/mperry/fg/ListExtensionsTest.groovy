package com.github.mperry.fg

import fj.F
import fj.F2
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
class ListExtensionsTest {

    @Test
    void join() {
        assertTrue(List.join([[1, 2], [3, 4], []]) == [1, 2, 3, 4])
    }

    @Test
    void map() {
        assertTrue([1, 2, 3].map({ Integer i -> i * 2} as F) == [2, 4, 6])
    }

    @Test
    void map2() {
        def f = { Integer i, Integer j -> i * j } as F2
        def list = List.map2([1, 2], [3, 4], f)
        assertTrue(list == [3, 4, 6, 8])
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void to() {
        def s = "a"
        def list = [1, 2].to(s)
        assertTrue([s, s] == list)
    }

    @Test
    void skip() {
        assertTrue([1, 2].skip() == [unit(), unit()])
    }

    @Test
    void foldM() {
        def list = List.foldM(1.to(3), 0, { Integer acc, Integer i -> [acc + i] } as F2)
        assertTrue(list == [6])
    }

    @Test
    void foldM_() {
        def list = List.foldM_(1.to(3), 0, { Integer acc, Integer i -> [acc + i] } as F2)
        assertTrue(list == [unit()])
    }

    @Test
    void sequence() {
        def list = List.sequence([[1, 2], [3, 4, 5]])
//        println list
        def expected = [[1, 3], [2, 3], [1, 4], [2, 4], [1, 5], [2, 5]]
        def haskell = [[1,3],[1,4],[1,5],[2,3],[2,4],[2,5]]
        println list
        assertTrue(list == haskell)
    }

    @Test
    void traverse() {
        def list = [1, 2, 3].traverse({ Integer i -> 1.to(i).toJList()} as F)
        println list
        def expected = [[1, 1, 1], [1, 2, 1], [1, 1, 2], [1, 2, 2], [1, 1, 3], [1, 2, 3]]

        def haskell = [[1,1,1],[1,1,2],[1,1,3],[1,2,1],[1,2,2],[1,2,3]]
        assertTrue(list == haskell)
    }

    @Test
    void replicateM() {
        def list = [1, 2].replicateM(3)
        println list
        def expected = [[1, 1, 1], [2, 1, 1], [1, 2, 1], [2, 2, 1], [1, 1, 2], [2, 1, 2], [1, 2, 2], [2, 2, 2]]
        def haskell = [[1,1,1],[1,1,2],[1,2,1],[1,2,2],[2,1,1],[2,1,2],[2,2,1],[2,2,2]]
        assertTrue(list == haskell)
    }

    @Test
    void compose() {
        def f = { String s -> [s, s] } as F
        def g = { Integer i ->
            1.to(i * 2).toJList().map({ Integer j ->
                j.toString()
            } as F)
        } as F
        def func = List.compose(f, g)
        def list = func.f(3)
        def expected = [1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6].collect { it.toString() }
//        println "$list == $expected ${list.class} ${expected.class}"
        assertTrue(list == expected)
    }

}
