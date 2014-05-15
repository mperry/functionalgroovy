package com.github.mperry.fg

import fj.F
import fj.F1Functions
import fj.F2
import fj.F2Functions
import fj.P1
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 12/04/2014.
 */
@TypeChecked
class Functions {

    static <A, B, C> F<A, F<B, C>> curry(F2<A, B, C> f) {
        F2Functions.curry(f)
    }

    static <A, B, C> F<C, B> o(F<A, B> f, F<C, A> g) {
        F1Functions.o(f, g)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    static <A, B, C> F<A, C> andThen(F<A, B> f, F<B, C> g) {
        Functions.o(g, f)
    }

    static <A, B> F<A, P1<B>> lazy(F<A, B> f) {
        F1Functions.lazy(f)
    }

}
