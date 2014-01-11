package com.github.mperry.fg

import fj.F
import fj.F2
import groovy.transform.TypeChecked
import org.junit.Test

import static com.github.mperry.fg.Writer.getLOG_FUNCTION
import static junit.framework.Assert.assertTrue

/**
 * Created by MarkPerry on 11/01/14.
 */
@TypeChecked
class WriterTest {

    @Test
    void test1() {
        def w = Writer.log(3).flatMap({ Integer a -> Writer.log(5).map({Integer b -> a * b} as F)} as F)
        println "writer: $w, value: ${w.value} log: ${w.log}"
        assertTrue(w.value == 15)
        assertTrue(w.log == LOG_FUNCTION.f(3) + LOG_FUNCTION.f(5))
    }


}
