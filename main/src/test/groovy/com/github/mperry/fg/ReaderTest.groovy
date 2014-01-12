package com.github.mperry.fg

import fj.F
import org.junit.Test

import static com.github.mperry.fg.Comprehension.foreach
import static junit.framework.Assert.assertTrue

/**
 * Created by MarkPerry on 11/01/14.
 */
class ReaderTest {

    @Test
    void test1() {
        def f = { Integer i -> i * 2} as F
        def g = { Integer i -> i + 10} as F
        def r = Reader.lift(f).flatMap({ Integer a -> Reader.lift({ Integer i -> i + 10 } as F)
                .map({Integer b -> a + b } as F)} as F)
        def z = r.f(3)
        println z
        assertTrue(z == 19)
    }

    @Test
    void test2() {
        def r = foreach {
            a << Reader.lift({ Integer i -> i * 2} as F)
            b << Reader.lift({ Integer i -> i + 10} as F)
            yield { a + b }
        }
        assertTrue(r.f(3) == 19)

    }

}
