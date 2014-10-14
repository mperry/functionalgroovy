package com.github.mperry.fg.typeclass.concrete

import fj.F
import fj.P2
import fj.data.State
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 15/10/2014.
 */
@TypeChecked
class StateInt<A> extends State<Integer, A> {

    StateInt(F<Integer, P2<Integer, A>> f) {
        run = f
    }

    def <B> StateInt<B> flatMap(F<A, StateInt<B>> f) {
        new StateInt<B>({ Integer s ->
            def p = run.f(s)
            def a = p._2()
            def s2 = p._1()

            def sib = f.f(a)
            sib.run.f(s2)
        } as F)
    }

    static <A> StateInt<A> unit(F<Integer, P2<Integer, A>> f) {
        new StateInt<A>(f)
    }

}
