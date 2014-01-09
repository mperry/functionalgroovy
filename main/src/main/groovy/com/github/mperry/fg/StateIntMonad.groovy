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
class StateIntMonad<A> extends Monad<StateMonad<Integer, A>>  {

    static F<Integer, P2<A, Integer>> run

    static StateMonad<Integer, Unit> empty() {
        StateMonad.lift({ Integer i -> P.p(Unit.unit(), i)} as F)
    }

    @Override
    def <B> StateMonad<Integer, B> flatMap(StateMonad<Integer, A> ma, F<A, StateMonad<Integer, B>> f) {
        StateMonad.lift({ Integer s ->
            def p = run.f(s)
            def a = p._1()
            def s1 = p._2()
            def smb = f.f(a)
            smb.run.f(s1)
        } as F)
    }

    @Override
    @TypeChecked(TypeCheckingMode.SKIP)
    def <B> StateMonad<Integer, B> unit(B b) {
        StateMonad.lift({ Integer s -> P.p(b, s) } as F)
    }

}
