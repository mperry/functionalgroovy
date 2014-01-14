package com.github.mperry.fg

import fj.F
import fj.F2
import fj.data.Either
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import static fj.data.Either.left

/**
 * Created by MarkPerry on 14/01/14.
 */
@TypeChecked
class SkiCalculus {

    def <X> X I(X x) {
        x
    }

    def <X, Y> F<Y, X> K(X x) {
        { Y y -> x } as F
    }

    def <X, Y, Z, A, B> B S(F<Z, F<A, B>> x, F<Z, A> y, Z z) {
        (x.f(z)).f(y.f(z))
    }

    /**
     * true
     * @param x
     * @param y
     * @return
     */
    @TypeChecked(TypeCheckingMode.SKIP)
    def <X, Y> X True(X x, Y y) {
        K(x).f(y)
    }

    def <X, Y> F2<X, Y, X> True_() {
        { X x, Y y -> True(x, y) } as F2
    }

    /**
     * false
     * @param x
     * @param y
     * @return
     */
    @TypeChecked(TypeCheckingMode.SKIP)
    def <X, Y> Y False(X x, Y y) {
        K(y).f(K(x).f(y))
    }

    def <X, Y> F2<X, Y, Y> False_() {
        { X x, Y y -> False(x, y) } as F2
    }

    def <A, B, C> Either<A, B> not(F2<A, B, C> f) {
        def val = f.f(False_(), True_())
        (val instanceof A) ? left(val) : Either.right(val)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def F2 or(F2 a, F2 b) {
        a.f(True_(), b)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def F2 and(F2 a, F2 b) {
        a.f(b, False_())
    }

}
