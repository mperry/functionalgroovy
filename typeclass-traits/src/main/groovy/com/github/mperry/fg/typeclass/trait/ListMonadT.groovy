package com.github.mperry.fg.typeclass.trait

import fj.F
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 15/04/2014.
 */

@TypeChecked
//@TypeChecked(TypeCheckingMode.SKIP)
class ListMonadT implements MonadT<List> {

//    def <A> List<A> unit(A a) {
//        [a]
//    }

    def <A> List<String> unit(A a) {
        [a.toString()]
    }

    def <A, B> List<B> flatMap(List<A> ma, F<A, List<B>> f) {
        ma.flatMap(f)
    }
}
