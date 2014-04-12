package com.github.mperry.fg

import fj.F
import groovy.transform.TypeChecked
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 20/12/13
 * Time: 8:52 AM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class FTest {

    @Test
    void test1() {
        def e = F.<Integer>unit{Integer i -> i + 1}
        def f = Functions.o(e, {Integer i -> i * 3} as F<Integer, Integer>)
        def g = F.<Integer>unit{Integer i -> i + 1}
        def h = Functions.o(g, {Integer i -> i * 3} as F)
        assertTrue(4  == f.f(1))
    }
}
