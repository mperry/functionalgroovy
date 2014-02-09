package com.github.mperry.fg

import fj.P
import fj.P2
import fj.data.Option
import fj.data.Stream
import groovy.transform.TypeChecked
import org.junit.Test

import static fj.P.p
import static fj.data.Option.some
import static org.junit.Assert.assertTrue

/**
 * Created by MarkPerry on 21/01/14.
 */
@TypeChecked
class StreamStaticExtension2Test {

    @Test
    void fib() {
        def s = Stream.unfold(p(1, 1)) { P2<Integer, Integer> p ->
            def low = p._1()
            def high = p._2()
            some(P.p(low, P.p(high, low + high)))
        }
        def list = s.take(10).toJavaList()
        def expected = [1, 1, 2, 3, 5, 8, 13, 21, 34, 55]
        assertTrue(list == expected)
    }


}
