package com.github.mperry.fg

import fj.F
import groovy.transform.TypeChecked
import org.junit.Test

//import static com.github.mperry.fg.FStaticExtension.unit
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
        def f = F.unit{ i -> i.toString()}
        assertTrue(4.toString() == f.f(4))
    }
}
