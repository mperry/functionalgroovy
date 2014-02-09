package com.github.mperry.fg

import fj.F
import fj.P
import fj.P2
import fj.Unit
import groovy.transform.Canonical
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 9/01/14.
 */
@TypeChecked
@Canonical
class StateIntMonad extends Monad<StateInt>  {

    @Override
    @TypeChecked(TypeCheckingMode.SKIP)
    def <B, C> StateInt<C> flatMap(StateInt<B> mb, F<B, StateInt<C>> f) {
        mb.flatMap(f)
    }

    @Override
    @TypeChecked(TypeCheckingMode.SKIP)
    def <B> StateInt<B> unit(B b) {
        new StateInt<B>({ Integer s -> P.p(b, s) } as F)
    }

}
