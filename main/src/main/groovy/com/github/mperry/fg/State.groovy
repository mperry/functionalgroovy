package com.github.mperry.fg

import fj.F
import fj.F1Functions
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
class State<S, A> {

    F<S, P2<S, A>> run

    P2<S, A> run(S s) {
        run.f(s)
    }

    static <S1, A1> State<S1, A1> lift(F<S1, P2<S1, A1>> f) {
        new State<S1, A1>(f)
    }

    static <S1> State<S1, S1> liftS(F<S1, S1> f) {
        State.<S1, S1>lift({ S1 s ->
            def s2 = f.f(s)
            P.p(s2, s2)
        } as F)
    }

    static <S1, A1> State<S1, A1> unit(A1 a) {
        lift({ S1 s -> P.p(s, a)} as F)
    }

    def <B> State<S, B> map(F<A, B> f) {
        State.lift({ S s ->
            def p2 = run(s)
            def b = f.f(p2._2())
            P.p(p2._1(), b)
        } as F)
    }

    static State<S, Unit> modify(F<S, S> f) {
        State.<S>get().flatMap { S s ->
            State.lift({ S s2 ->
//                def s3 = f.f(s)
                P.p(f.f(s), Unit.unit())
            } as F)

        }
    }

    def <B> State<S, B> map(Closure<B> c) {
        map(c as F)
    }

    def <B> State<S, B> mapState(F<P2<S, A>, P2<S, B>> f) {
        new State<S, B>({ S s ->
            def p = run(s)
            f.f(p)
        } as F)
    }

    static <S, B, C> State<S, C> flatMap(State<S, B> mb, F<B, State<S, C>> f) {
        mb.flatMap(f)
    }


    def <B> State<S, B> flatMap(F<A, State<S, B>> f) {
        new State<S, B>({ S s ->
            def p = run(s)
            def a = p._2()
            def s2 = p._1()
            def smb = f.f(a)
            smb.run(s2)
        } as F)
    }

    def <B> State<S, B> flatMap(Closure<State<S, B>> c) {
        flatMap(c as F)
    }

    def <S1, A1> State<S1, A1> unit(F<S1, P2<S1, A1>> f) {
        lift(f)
    }

    static <S1> State<S1, S1> get() {
        def f = { S1 s -> P.p(s, s) }
        State.<S1>lift(f as F)
    }

    State<S, S> gets() {
        State.lift({ S s ->
            def p = run(s)
            def s2 = p._1()
            P.p(s2, s2)
        } as F)
    }

    static <S1> State<S1, Unit> put(S1 s) {
        State.lift({ Object z -> P.p(s, Unit.unit())} as F)
    }

    A eval(S s) {
        run(s)._2()
    }

    S exec(S s) {
        run(s)._1()
    }

    State<S, A> withs(F<S, S> f) {
        lift(F1Functions.andThen(f, run))
    }

    static State<S, A> gets(F<S, A> f) {
        State.get().map({ S s -> f.f(s)} as F)
    }

}
