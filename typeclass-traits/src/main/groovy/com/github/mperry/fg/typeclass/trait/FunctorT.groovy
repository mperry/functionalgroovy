package com.github.mperry.fg.typeclass.trait

import fj.F
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 9/04/2014.
 * @see http://hackage.haskell.org/package/base-4.7.0.0/docs/Data-Functor.html
 * @see http://www.haskell.org/haskellwiki/Typeclassopedia
 *
 * Laws:
 * fmap id = id
 * fmap (g . h) = (fmap g) . (fmap h)
 */
@TypeChecked
trait FunctorT<T> {

    /**
     * fmap :: (a -> b) -> f a -> f b
     */
    abstract <A, B> T<B> fmap(F<A, B> f, T<A> fa)

}
