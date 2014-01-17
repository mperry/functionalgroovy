package com.github.mperry.fg

import fj.F
import fj.F2
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

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

}
