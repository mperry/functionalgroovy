package com.github.mperry.fg

import fj.P1
import fj.control.parallel.Strategy
import fj.data.Option
import groovy.transform.TypeChecked
import org.junit.Test

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

import static com.github.mperry.fg.test.Specification.specAssert
import static org.junit.Assert.assertTrue

/**
 * Created by MarkPerry on 17/04/2014.
 */

@TypeChecked
class FutureTest {

    Future<String> future(String s) {
        Executors.newCachedThreadPool().submit(callable(s))
    }

    Callable<String> callable(final String s) {
        new Callable<String>() {
            @Override
            String call() throws Exception {
                Thread.sleep(100) // milliseconds
                s
            }
        }
    }

    P1<Option<Integer>> transform(String val) {
        def p = Strategy.obtain(future(val))
        p.map { String s -> Option.parseInt.f(s) }
    }

    Boolean truth(String s) {
        // mapping the product of the future equals calling the function directly
        transform(s)._1() == Option.parseInt.f(s)
    }

    @Test
    void three() {
        assertTrue(truth("3"))
    }

    @Test
    void simple() {
        ["not an int", "3", " 3", "3 "].each { String s ->
            def b = truth(s)
//            println("s: $s, b: $b")
            assertTrue(b)
        }
    }

    @Test
    void spec() {
        specAssert { String s -> truth(s) }
    }

}
