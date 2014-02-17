package com.github.mperry.fg

import fj.P2
import groovy.transform.TypeChecked
import org.junit.Test

/**
 * Created by MarkPerry on 17/02/14.
 */
@TypeChecked
class RandTest {

    @Test
    void test1() {
        def r = new Rand(42L)
        def p = r.nextInt()
        println(p)
        def p2 = p._2().nextInt()
        println(p2)

    }

    def <A, B> void println(P2<A, B> p) {
        println("<${p._1()}, ${p._2()}>")
    }

}
