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
        def param = [1, 2, 3]
        def c = { Integer i -> i * 2 }
        def actual = f.fmap(c, param)
        assertTrue(actual == param.collect(c))
    }

}
