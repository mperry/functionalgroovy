package com.github.mperry.fg.typeclass.concrete

import com.github.mperry.fg.typeclass.Monad
import fj.F
import fj.P
import fj.data.State
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 15/10/2014.
 */
@TypeChecked
class StateIntMonad extends Monad<StateInt> {

    @Override
    def <A, B> StateInt<B> flatMap(StateInt<A> ma, F<A, StateInt<B>> f) {
        ma.flatMap(f)
    }

    @Override
    def <A> StateInt<A> unit(A a) {
        StateInt.<A>unit({ Integer i -> P.p(i, a)})
    }
}
