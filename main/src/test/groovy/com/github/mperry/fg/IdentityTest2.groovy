package com.github.mperry.fg

import fj.F
import fj.F2
import fj.Unit
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

import static com.github.mperry.fg.Identity.lift
import static junit.framework.Assert.assertTrue

/**
 * Created by MarkPerry on 18/04/2014.
 */
@TypeChecked
class IdentityTest2 {

    Identity<Unit> monad() {
        Identity.idUnit()
    }

    @Test
    void join() {
        def n = 3
        assertTrue(monad().join(lift(lift(n))) == lift(n))
    }

    @Test
    void map() {
        def a = 2
        def b = 3
        def f = { Integer i -> a * i } as F
        assertTrue(monad().map(lift(b), f) == lift(a * b))
    }

    @Test
    void map2() {
        assertTrue(monad().map2(lift(2), lift(3), { Integer a, Integer b -> a * b}) == lift(6))
    }

    @Test
    void to() {
        assertTrue(monad().to(lift(3), "abc") == lift("abc"))
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void foldM() {
        assertTrue(monad().foldM(1.to(5), 1.toInteger(), { Integer a, Integer b -> lift(a * b)}) == lift(120))
    }

    @Test
    void sequence() {
        assertTrue(monad().sequence([lift(2), lift(3), lift(4)]) == lift([2, 3, 4]))
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void traverse() {
        F<Integer, Identity<Integer>> f = { Integer i -> lift(i + 1) }  as F
//        as F<Integer, Identity<Integer>>
//        as F
//        as F<Integer, Identity<Integer>>
        def actual = monad().traverse([2, 3, 4], f)
        def expected = lift([3, 4, 5])
        assertTrue(actual == expected)
    }

}
