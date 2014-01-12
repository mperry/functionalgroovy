package com.github.mperry.fg

import fj.F
import fj.P
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

import static com.github.mperry.fg.Comprehension.foreach

/**
 * Created by MarkPerry on 13/01/14.
 */
@TypeChecked
class StateTest {

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void test1() {
        def s = State.lift({ Random r -> P.p(r.nextBoolean(), r) } as F)
        def s2 = foreach {
            a << s
            b << s
            c << s
            d << s
            yield { [a, b, c, d] }
        }
        def p = s2.run.f(new Random(0))
        println "<${p._1()}, ${p._2()}>"
    }

}
