package com.github.mperry.fg.typeclass

import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 27/12/2014.
 *
 * class MonadTrans t where
 * lift :: Monad m => m a -> t m a
 */
@TypeChecked
abstract class MonadTrans<T> {

    abstract <A, M> T<M, A> lift(M<A> m, Monad<M> monad)

}
