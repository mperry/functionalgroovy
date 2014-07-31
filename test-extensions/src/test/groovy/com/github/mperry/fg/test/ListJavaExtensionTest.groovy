package com.github.mperry.fg.test

import fj.F
import fj.F2
import fj.P
import fj.P1
import fj.P2
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

import static fj.P.*
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
    void listFoldRight() {
        def original = 1.to(4).toJavaList()
        def list = original.foldRightT([], { Integer i, List<Integer> list ->
            list + [i]
        })
//        println list
        assertTrue(list == original.reverse())
    }

    @Test
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
            list.foldRightR(0, { Integer i, Integer acc -> acc + i } as F2)
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
            seed > max ? none() : some(p(seed, seed + 1))
        } as F)
        assertTrue(list == (1..max).toList())
    }

    @Test
    void unfoldClosure() {
        def max = 5
        def list = List.unfold(1) { Integer it -> it > max ? none() : some(p(it, it + 1)) }
        assertTrue(list == 1.to(max).toJavaList())
    }

    @Test
    void testCollate() {
        def list = collate([1, 2, 3 ,4], 3, 1)
        def list2 = collate2([1, 2, 3 ,4], 3, 1)
        def list3 = collate3([1, 2, 3 ,4], 3)
//        println list
        def expected = [[1, 2, 3], [2, 3, 4], [3, 4], [4]]
        assertTrue(list == expected)
        assertTrue(list2 == expected)
        assertTrue(list3 == expected)

    }

    /**
     * Implement collate using unfold
     */
    List<List<Integer>> collate(List<Integer> list, int size, int step) {
        List.unfold(list) { List<Integer> it ->
           it.empty ? none() : some(p(it.take(size), it.drop(step)))
        }
    }

    /**
     * Implement collate using unfold using minimal characters
     */
    @TypeChecked(TypeCheckingMode.SKIP)
    List<List<Integer>> collate2(List<Integer> list, int size, int step) {
        List.unfold(list) {
            it.empty ? none() : some(p(it.take(size), it.drop(step)))
        }
    }

    /**
     * Implement collate using Java 8 compatible operations, take/limit, drop/skip
     * Java 8 did have zip for streams
     * http://download.java.net/lambda/b78/docs/api/java/util/stream/Streams.html
     * but does not look like it exists for latest version
     * http://download.java.net/jdk8/docs/api/
     */
    List<List<Integer>> collate3(List<Integer> list, int size) {
        def dropList = 0..list.size() - 1
        def zipped = List.repeat(list.size(), list).zip(dropList)
        def result = zipped.map { P2<List<Integer>, Integer> p ->
            p._1().drop(p._2()).take(size)
        }
        result
    }

    @Test
    void intersperse() {
        assertTrue([].intersperse(0) == [])
        assertTrue([1].intersperse(0) == [1])
        assertTrue([1, 2, 3].intersperse(0) == [1, 0, 2, 0, 3])
    }

}
