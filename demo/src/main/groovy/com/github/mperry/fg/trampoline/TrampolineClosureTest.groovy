package com.github.mperry.fg.trampoline

import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test
import org.junit.Assert

/**
 * Created by MarkPerry on 21/01/14.
 */
@TypeChecked
class TrampolineClosureTest {

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void test1() {
        def fact
        fact = { acc, it ->
            it <= 1 ? acc : fact.trampoline(it * acc, it - 1)
        }.trampoline()
        def v = fact.call(1, 3)
        println v
        Assert.assertTrue(v == 6)

    }

}
