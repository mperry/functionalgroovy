package com.github.mperry.fg.typeclass.concrete

import com.github.mperry.fg.typeclass.Functor
import fj.F

/**
 * Created by MarkPerry on 10/04/2014.
 */
class SetFunctor<A> implements Functor<Set<A>> {

    @Override
    def <A, B> Set<B> fmap(F<A, B> f, Set<A> fa) {
        fa.collect(f.toClosure())
    }

}
