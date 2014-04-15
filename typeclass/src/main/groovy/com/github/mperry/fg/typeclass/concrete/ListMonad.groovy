package com.github.mperry.fg.typeclass.concrete

import com.github.mperry.fg.typeclass.Monad
import fj.F
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 10/04/2014.
 */
@TypeChecked
class ListMonad extends Monad<List> {

    @Override
    def <A, B> List<B> flatMap(List<A> ma, F<A, List<B>> f) {
        ma.flatMap(f)
    }

    @Override
    def <B> List<B> unit(B b) {
        [b]
    }
}
