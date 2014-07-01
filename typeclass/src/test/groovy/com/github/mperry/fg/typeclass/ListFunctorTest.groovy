package com.github.mperry.fg.typeclass

import com.github.mperry.fg.typeclass.concrete.ListFunctor
import groovy.transform.TypeChecked
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * Created by MarkPerry on 10/04/2014.
 */
@TypeChecked
class ListFunctorTest {

    @Test
    void test1() {
        def f = new ListFunctor()
        def param = [1, 2, 3]
        def c = { Integer i -> i * 2 }
        def actual = f.map(c, param)
        assertTrue(actual == param.collect(c))
    }

}
