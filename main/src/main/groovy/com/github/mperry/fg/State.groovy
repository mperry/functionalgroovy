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
class State<S, A> {

    F<S, P2<A, S>> run

    @TypeChecked(TypeCheckingMode.SKIP)
    static <S1, A1> State<S1, A1> lift(F<S1, P2<A1, S1>> f) {
        new State<S1, A1>(f)
    }

    @Override
    @TypeChecked(TypeCheckingMode.SKIP)
    def <B, C, D> State<S, C> flatMap(State<S, B> mb, F<B, State<S, C>> f) {
        mb.flatMap(f)
    }

    @Override
    def <B> State<S, B> flatMap(F<A, State<S, B>> f) {
        new State<S, B>({ S s ->
            def p = run.f(s)
            def a = p._1()
            def s2 = p._2()
            def smb = f.f(a)
            smb.run.f(s2)
        } as F)
    }

    @Override
    def <S1, A1> State<S1, A1> unit(F<S1, P2<A1, S1>> f) {
        lift(f)
    }

}
