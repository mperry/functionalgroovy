package com.github.mperry.fg

import groovy.transform.TypeChecked
import org.junit.Assert
import org.junit.Test

/**
 * Created by MarkPerry on 23/01/14.
 */
//@TypeChecked
class YCombinatorTest {

    @Test
    void testFib() {

        Closure fib = {Closure f ->
            { int n ->
                n < 2 ? n: f(n-1) + f(n-2)
            }
        }

        def f = YCombinator.Y(fib)
        def x = f(6)

        println x
        Assert.assertTrue(x == 8)
    }

}
