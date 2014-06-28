package com.github.mperry.fg.typeclass.concrete

import com.github.mperry.fg.typeclass.Monad
import fj.F
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 10/04/2014.
 */
@TypeChecked
//@TypeChecked(TypeCheckingMode.SKIP)
class ListMonad<A> extends Monad<List> {

    @Override
    def <B> List<B> flatMap(List<A> ma, F<A, List<B>> f) {
        ma.flatMap(f)
    }

    @Override
    def List<A> unit(A a) {
        [a]
    }
}
