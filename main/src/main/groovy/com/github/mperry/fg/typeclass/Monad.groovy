package com.github.mperry.fg.typeclass

import fj.F
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 9/04/2014.
 */
@TypeChecked
interface Monad<M> {

    /**
     *  return :: a -> m a
     */
    def <A> M<A> unit(A a)

    /**
     * (>>=)  :: m a -> (a -> m b) -> m b
     */
    def <A, B> M<B> flatMap(M<A> ma, F<A, M<B>> f)

}
