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
class StateInt<A> extends State<Integer, A> {

    StateInt(F<Integer, P2<A, Integer>> f) {
        run = f
    }

}
