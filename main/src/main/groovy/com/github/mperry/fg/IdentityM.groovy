package com.github.mperry.fg

import com.github.mperry.fg.typeclass.Monad
import fj.F
import fj.Unit
import groovy.transform.Canonical
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 9/01/14.
 */
@TypeChecked
@Canonical
class IdentityM<A> extends Monad<IdentityM> {

    A item

    private IdentityM(A a) {
        item = a
    }

    static IdentityM<Unit> idUnit() {
        lift(Unit.unit())
    }

    static <B> IdentityM<B> lift(B b) {
        new IdentityM<B>(b)
    }

//    @TypeChecked(TypeCheckingMode.SKIP)
    @Override
    def <B, C> IdentityM<C> flatMap(IdentityM<B> mb, F<B, IdentityM<C>> f) {
        f.f(mb.item)
    }

    @Override
    def <B> IdentityM<B> unit(B b) {
        lift(b)
    }

    def <B> IdentityM<B> flatMap(F<A, IdentityM<B>> f) {
        flatMap(this, f)
    }


}
