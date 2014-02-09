package com.github.mperry.fg

import fj.F
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 11/01/14.
 */
@TypeChecked
class ReaderM<A, B> {

    F<A, B> function

    ReaderM(F<A, B> f) {
        function = f
    }

    B f(A a) {
        function.f(a)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    static <C, D> ReaderM<C, D> lift(F<C, D> f) {
        new ReaderM(f)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    static <C, D> ReaderM<C, D> lift(Closure<D> f) {
        new ReaderM(f as F)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <C> ReaderM<A, C> map(F<B, C> f) {
        lift(function.andThen(f))
    }

    def <C> ReaderM<A, C> andThen(F<B, C> f) {
        map(f)
    }

    def <C> ReaderM<A, C> map(Closure<C> c) {
        map(c as F)
    }

    def <C> ReaderM<A, C> flatMap(F<B, ReaderM<A, C>> f) {
        lift({A a -> f.f(function.f(a)).f(a)} as F)
    }

    def <C> ReaderM<A, C> flatMap(Closure<ReaderM<A, C>> c) {
        flatMap(c as F)
    }



}
