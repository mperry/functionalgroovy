package com.github.mperry.fg

import fj.F
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 11/01/14.
 */
@TypeChecked
class Reader<A, B> {

    F<A, B> function

    Reader(F<A, B> f) {
        function = f
    }

    B f(A a) {
        function.f(a)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    static <C, D> Reader<C, D> lift(F<C, D> f) {
        new Reader(f)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
   def <C> Reader<A, C> map(F<B, C> f) {
        lift(function.andThen(f))
    }

    def <C> Reader<A, C> flatMap(F<B, Reader<A, C>> f) {
        lift({A a -> f.f(function.f(a)).f(a)} as F)
    }



}
