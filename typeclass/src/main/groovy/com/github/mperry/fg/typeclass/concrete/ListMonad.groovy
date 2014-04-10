package com.github.mperry.fg.typeclass.concrete

import com.github.mperry.fg.typeclass.Monad
import fj.F

/**
 * Created by MarkPerry on 10/04/2014.
 */
class ListMonad<A> extends Monad<List<A>> {

    @Override
    def <A, B> List<B> flatMap(List<A> ma, F<A, List<B>> f) {
        ma.flatMap(f)
    }

    @Override
    def <B> List<B> unit(B b) {
        [b]
    }
}
