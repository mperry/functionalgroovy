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
import static java.util.List.*
import static org.junit.Assert.assertTrue

/**
 * Created by MarkPerry on 17/01/14.
 */
@TypeChecked
class ListJavaExtensionTest {

    @Test
    void foldListLeft() {
        assertTrue([1, 2, 3].foldLeft(0, { Integer acc, Integer i -> acc + i} as F2) == 6)
        assertTrue([1, 2, 3].foldLeft(0) { Integer acc, Integer i -> acc + i } == 6)
    }

    @Test
    void foldListLeftR() {
        def c = { Integer acc, Integer i -> acc + i}
        assertTrue([1, 2, 3].foldLeftR(0, c as F2) == 6)
        assertTrue([1, 2, 3].foldLeftR(0, c) == 6)
        def max = 10 ** 4
        def p = { ->
            (1..max).toList().foldLeftR(0, c)
        } as P1
//        assertTrue(p._1() == 6)
        assertTrue(p.throwsError(StackOverflowError.class))
//        assertTrue(p._1() == 6)
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
    void foldRightNoOverflow2() {
        def high = (Integer) 10 ** 4
        def list = (1..high).toList()
        def val = list.foldRightT(0, { Integer acc, Integer i -> acc + i } as F2)
        def expected = 50005000
        assertTrue(val == expected)
    }


    @Test
    void unfold() {
        def max = 10
        def list = List.unfold(1, { Integer seed ->
            seed > max ? none() : some(P.p(seed, seed + 1))
        } as F)
        assertTrue(list == (1..max).toList())
    }

    @Test
    void unfoldClosure() {
        def max = 5
        def list = List.unfold(1) { Integer it -> it > max ? none() : some(P.p(it, it + 1)) }
        assertTrue(list == 1.to(max).toJavaList())
    }

}
