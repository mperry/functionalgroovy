package com.github.mperry.fg.typeclass

import fj.F
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 27/12/2014.
 *
 * class MonadTrans t where
 * lift :: Monad m => m a -> t m a
 */
@TypeChecked
abstract class MonadTrans<T> {

    abstract <A, M> T<M, A> unit(M<A> m, Monad<M> monad)

    abstract <M, A, B> T<M, B> flatMap(T<M, A> t, Monad<M> m, F<A, T<M, B>> f)


}
