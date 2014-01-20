package com.github.mperry.fg.test

import fj.F
import fj.F2
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * Created by MarkPerry on 17/01/14.
 */
@TypeChecked
class ListJavaExtensionTest {

    @Test
    void foldLeft() {
        assertTrue([1, 2, 3].foldLeft(0, { Integer acc, Integer i -> acc + i} as F2) == 6)
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void listFold() {
        def list = 1.to(4).toJList().foldLeft([], { List<Integer> list, Integer i ->
            [i] + list
        } as F2)
        println list

    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void listFoldRight() {
        def list = 1.to(4).toJList().foldRightF([], { List<Integer> list, Integer i ->
            [i] + list
        } as F2)
        println list

    }

    @Test
//    @TypeChecked(TypeCheckingMode.SKIP)
    void foldRightT() {
//        def list = [1, 2, 3].foldRight(0, { Integer acc, Integer i -> acc + i} as F2)
        def val = [1, 2, 3].foldRightTrampoline(0, { Integer i, Integer acc -> i + acc } as F2)
        assertTrue(val.run() == 6)
    }



}
