package com.github.mperry.fg.typeclass

import fj.F
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 9/04/2014.
 * @see http://www.haskell.org/haskellwiki/Typeclassopedia
 */
@TypeChecked
interface Functor<T> {

    /**
     * fmap :: (a -> b) -> f a -> f b
     * Laws:
     * fmap id = id
     * fmap (g . h) = (fmap g) . (fmap h)
     */
    abstract <A, B> T<B> fmap(T<A> fa, F<A, B> f)




}
