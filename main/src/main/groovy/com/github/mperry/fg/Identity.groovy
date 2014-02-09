package com.github.mperry.fg

import fj.F
import fj.Unit
import groovy.transform.Canonical
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 9/01/14.
 */
@TypeChecked
@Canonical
class Identity<A> extends Monad<Identity> {

    A item

    private Identity(A a) {
        item = a
    }

    static Identity<Unit> idUnit() {
        lift(Unit.unit())
    }

    static <B> Identity<B> lift(B b) {
        new Identity<B>(b)
    }

    @Override
    @TypeChecked(TypeCheckingMode.SKIP)
    def <B, C> Identity<C> flatMap(Identity<B> mb, F<B, Identity<C>> f) {
        f.f(mb.item)
    }

    @Override
    def <B> Identity<B> unit(B b) {
        lift(b)
    }

    @Override
    def <B> Identity<B> flatMap(F<A, Identity<B>> f) {
        flatMap(this, f)
    }


}
