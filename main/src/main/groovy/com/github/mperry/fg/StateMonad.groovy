package com.github.mperry.fg

import fj.F
import fj.P2
import groovy.transform.Canonical
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 9/01/14.
 */
@TypeChecked
@Canonical
class StateMonad<S, A> {

    static F<S, P2<A, S>> run

    StateMonad(F<S, P2<A, S>> f) {
        run = f
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    static <S1, A1> StateMonad<S1, A1> lift(F<S1, P2<A1, S1>> f) {
        new StateMonad<S1, A1>(f)
    }

    @Override
    def <A, B> StateMonad<S, B> flatMap(StateMonad<S, A> ma, F<A, StateMonad<S, B>> f) {
        new StateMonad<S, B>({ S s ->
            def p = run.f(s)
            def a = p._1()
            def s1 = p._2()
            def smb = f.f(a)
            smb.run.f(s1)
        } as F)
    }

    @Override
    def <S1, A1> StateMonad<S1, A1> unit(F<S1, P2<A1, S1>> f) {
        lift(f)
    }



}
