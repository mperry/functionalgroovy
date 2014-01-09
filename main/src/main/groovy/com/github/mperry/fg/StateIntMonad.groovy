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
class StateIntMonad<A> extends Monad<State<Integer, A>>  {

    static F<Integer, P2<A, Integer>> run


    StateIntMonad(F<Integer, P2<A, Integer>> f) {
        run = f
    }


    static StateIntMonad<Unit> empty() {
        StateIntMonad.lift({ Integer i -> P.p(Unit.unit(), i)} as F)
    }


    @TypeChecked(TypeCheckingMode.SKIP)
    static <A1> StateIntMonad<A1> lift(F<Integer, P2<A1, Integer>> f) {
        new StateIntMonad<A1>(f)
    }


    @Override
    def <B> State<Integer, B> flatMap(State<Integer, A> ma, F<A, State<Integer, B>> f) {
        State.lift({ Integer s ->
            def p = run.f(s)
            def a = p._1()
            def s1 = p._2()
            def smb = f.f(a)
            smb.run.f(s1)
        } as F)
    }

    @Override
    @TypeChecked(TypeCheckingMode.SKIP)
    def <B> State<Integer, B> unit(B b) {
        State.lift({ Integer s -> P.p(b, s) } as F)
    }

}
