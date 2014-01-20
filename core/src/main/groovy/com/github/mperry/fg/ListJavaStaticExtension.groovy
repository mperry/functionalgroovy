package com.github.mperry.fg

import fj.F
import fj.P2
import fj.data.Option
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 30/12/13.
 */
@TypeChecked
class ListJavaStaticExtension {

    @TypeChecked(TypeCheckingMode.SKIP)
    public static <A> java.util.List<A> repeat(List list, Integer n, A a) {
        def result = []
        for (int i = 0; i < n; i++) {
            result.add(a)
        }
        result
    }

    /**
     * Similar to fj.data.Stream, takes constant stack size
     * @param list
     * @param b
     * @param f
     * @return
     */
//    @TypeChecked(TypeCheckingMode.SKIP)
    static <A, B> List<A> unfold(List list, B b, F<B, Option<P2<A, B>>> f) {
        def result = []
        def val = b
        def loop = true
        while (loop) {
            def o = f.f(val)
            if (o.isNone()) {
                loop = false
            } else {
                def p = o.some()
                result.add(p._1())
                val = p._2()
            }
        }
        result
    }

}
