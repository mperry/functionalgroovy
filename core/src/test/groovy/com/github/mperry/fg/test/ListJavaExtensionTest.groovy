package com.github.mperry.fg.test

import fj.F
import fj.F2
import fj.P
import fj.P1
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

import static fj.data.Option.none
import static fj.data.Option.some
import static org.junit.Assert.assertTrue

/**
 * Created by MarkPerry on 17/01/14.
 */
@TypeChecked
class ListJavaExtensionTest {

    @Test
    void foldListLeft() {
        assertTrue([1, 2, 3].foldLeft(0, { Integer acc, Integer i -> acc + i} as F2) == 6)
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void foldListLeftCons() {
        def val = 1.to(4).toJavaList()
        def act = val.foldLeft([], { List<Integer> list, Integer i ->
            list + [i]
        } as F2)
        assertTrue(val == act)
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void listFoldRight() {
        def original = 1.to(4).toJavaList()
        def list = original.foldRightT([], { List<Integer> list, Integer i ->
            list + [i]
        } as F2)
//        println list
        assertTrue(list == original.reverse())
    }

    @Test
//    @TypeChecked(TypeCheckingMode.SKIP)
    void foldRightTrampoline() {
        def val = [1, 2, 3].foldRightTrampoline(0, { Integer acc, Integer i -> i + acc } as F2)
        assertTrue(val.run() == 6)
    }

    @Test
    void foldRightT() {
        def val = [1, 2, 3].foldRightT(0, { Integer acc, Integer i -> i + acc } as F2)
        assertTrue(val == 6)
    }

     @Test
    void foldRightOverflow() {
        def high = (Integer) 10 ** 4
        def list = 1.to(high).toJavaList()
        def p = { ->
            list.foldRightF(0, { Integer acc, Integer i -> acc + i } as F2)
        } as P1
        assertTrue(p.throwsError(StackOverflowError.class))
    }

    @Test
    void foldRightNoOverflow() {
        def high = (Integer) 10 ** 4
        def list = 1.to(high).toJavaList()
        def p = { ->
            list.foldRightT(0, { Integer acc, Integer i -> acc + i } as F2)
        } as P1
        def expected = 50005000
//        assertTrue(!p.throwsError(StackOverflowError.class))
        def val = p._1()
//        println val
        assertTrue(val == expected)
    }

    @Test
//    @TypeChecked(TypeCheckingMode.SKIP)
    void unfold() {
        def max = 10
        def list = List.unfold(1, { Integer i ->
            i > max ? none() : some(P.p(i, i + 1))
        } as F)
//        println list
        assertTrue(list == 1.to(max).toJavaList())

    }

}
