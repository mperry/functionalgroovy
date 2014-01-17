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
//        println list
        assertTrue(list == [3, 4, 6, 8])
    }

    @Test
    void skip() {
        assertTrue([1, 2].skip() == [unit(), unit()])
    }

    @Test
    void traverse() {
        def list = [1, 2, 3].traverse({ Integer i -> 1.to(i).toJList()} as F)
        println list
//        assertTrue(list == [[1], [1, 2], [1, 2, 3]])
//        assertTrue(list == [[1,1,1],[1,1,2],[1,1,3],[1,2,1],[1,2,2],[1,2,3]])

    }

    @Test
    void as_() {
        def s = "a"
//        [1].as_(s)
//        assertTrue([1].as_(s) == [s])
    }


}
