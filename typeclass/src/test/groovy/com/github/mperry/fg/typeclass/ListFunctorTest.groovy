package com.github.mperry.fg.typeclass

import com.github.mperry.fg.typeclass.concrete.ListFunctor
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * Created by MarkPerry on 10/04/2014.
 */
class ListFunctorTest {

    @Test
    void test1() {
        def f = new ListFunctor<List<Integer>>()
        def actual = f.fmap({ Integer i -> i * 2}, [1, 2, 3])
        assertTrue(actual == [2, 4, 6])
    }

}
