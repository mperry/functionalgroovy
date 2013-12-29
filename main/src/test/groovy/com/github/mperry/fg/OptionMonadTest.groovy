package com.github.mperry.fg

import fj.F
import fj.data.Option
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * Created by MarkPerry on 30/12/13.
 */
class OptionMonadTest {

    OptionMonad<Integer> monad() {
        new OptionMonad<Integer>()
    }

    @Test
    void testUnit() {
        def m = new OptionMonad<Integer>()
        def o1 = m.unit(3)
        assertTrue(o1.some() == 3)
    }

    @Test
    void testFlatMap() {
        def m = new OptionMonad<Integer>()
        def o1 = m.unit(3)
        def o2 = m.flatMap(o1, {Integer i -> Option.some(2 * i)} as F)
        assertTrue(o2.some() == 6)
//        o2

    }

    @Test
    void testMap() {
        def m = monad()
        def o2 = m.map(m.unit(3), { Integer i -> (2 * i).toString()} as F)
        assertTrue(o2.some() == 6.toString())
    }


}
