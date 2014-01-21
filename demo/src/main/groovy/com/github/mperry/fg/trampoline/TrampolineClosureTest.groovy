package com.github.mperry.fg.trampoline

import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test
import org.junit.Assert
import fj.F2

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

    @Test
    void test2() {
        def list = [1, 2, 3]
        def v = foldRight(list, 1, { Integer acc, Integer i -> acc * i } as F2)
        println v
        Assert.assertTrue(v == 6)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
//    def <B> foldRight(List<A> list, B b, F2<B, A, B> f) {
    def <A, B> B foldRight(List<A> list, B b, F2<B, A, B> f) {

        def rec
        rec = { List l, B myB, F2 myF ->
            def val = rec.call(list.tail(), myB, myF)
            f.f(val, list.head)
        }.trampoline()
        def v = rec.call(list, b, f)
        println v
        v

    }

}
