package com.github.mperry.fg

import fj.F
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
//        assertTrue(List.join([[1, 2], [3, 4], []]) == [1, 2, 3, 4])
    }

    @Test
   void map() {
//        assertTrue([1, 2, 3].map({ Integer i -> i * 2} as F) == [2, 4, 6])
   }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void map2() {
//        def list1 = new ArrayList<Integer>()
//        list1.addAll([1, 2, 3])
//        def list2 = [1, 2, 3]
//        assertTrue(list2.map2([3, 4, 5], {Integer i, Integer j -> i * j} as F) == [3, 8, 15])
    }

    @Test
    void map3() {
//        assertTrue([1, 2, 3].map3({ Integer i -> i * 2} as F) == [2, 4, 6])
    }

}
