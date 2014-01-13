package com.github.mperry.fg

import fj.F
import fj.P2
import groovy.transform.Canonical
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 9/01/14.
 */
@TypeChecked
@Canonical
class StateInt<A> extends StateM<Integer, A> {

    StateInt(F<Integer, P2<A, Integer>> f) {
        run = f
    }

    def <B, C> StateInt<B> flatMap(F<A, StateInt<B>> f) {
        new StateInt<B>({ Integer s ->
            def p = run.f(s)
            def a = p._1()
            def s2 = p._2()

            def sib = f.f(a)
            sib.run.f(s2)
        } as F)
    }

}
