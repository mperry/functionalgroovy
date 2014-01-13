package com.github.mperry.fg

import fj.F
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

import static com.github.mperry.fg.Comprehension.foreach
import static WriterM.getLOG_FUNCTION
import static junit.framework.Assert.assertTrue

/**
 * Created by MarkPerry on 11/01/14.
 */
@TypeChecked
class WriterTest {

    @Test
    void test1() {
        def w = WriterM.log(3).flatMap({ Integer a -> WriterM.log(5).map({Integer b -> a * b} as F)} as F)
        println "writer: $w, value: ${w.value} log: ${w.log}"
        assertTrue(w.value == 15)
        assertTrue(w.log == LOG_FUNCTION.f(3) + LOG_FUNCTION.f(5))
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void test2() {
        def w = foreach {
            a << WriterM.log(3)
            b << { WriterM.log(5) }
            yield { a * b }
        }
        assertTrue(w.value == 15)
        assertTrue(w.log == LOG_FUNCTION.f(3) + LOG_FUNCTION.f(5))



    }

}
