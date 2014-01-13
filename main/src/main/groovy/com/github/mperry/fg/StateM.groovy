package com.github.mperry.fg

import fj.F
import fj.P
import fj.P2
import groovy.transform.Canonical
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 9/01/14.
 */
@TypeChecked
@Canonical
class StateM<S, A> {

    F<S, P2<A, S>> run

    P2<A, S> run(S s) {
        run.f(s)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    static <S1, A1> StateM<S1, A1> lift(F<S1, P2<A1, S1>> f) {
        new StateM<S1, A1>(f)
    }

    def <B> StateM<S, B> map(F<A, B> f) {
        StateM.lift({ S s ->
            def p2 = run.f(s)
            def b = f.f(p2._1())
            P.p(b, p2._2())
        } as F)
    }

    def <B> StateM<S, B> map(Closure<B> c) {
        map(c as F)
    }

    @Override
    @TypeChecked(TypeCheckingMode.SKIP)
    def <B, C, D> StateM<S, C> flatMap(StateM<S, B> mb, F<B, StateM<S, C>> f) {
        mb.flatMap(f)
    }


    @Override
    def <B> StateM<S, B> flatMap(F<A, StateM<S, B>> f) {
        new StateM<S, B>({ S s ->
            def p = run.f(s)
            def a = p._1()
            def s2 = p._2()
            def smb = f.f(a)
            smb.run.f(s2)
        } as F)
    }

    @Override
    def <B> StateM<S, B> flatMap(Closure<StateM<S, B>> c) {
        flatMap(c as F)
    }

    @Override
    def <S1, A1> StateM<S1, A1> unit(F<S1, P2<A1, S1>> f) {
        lift(f)
    }

}
