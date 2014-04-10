package com.github.mperry.fg.typeclass

import fj.F
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 9/04/2014.
 * @see http://www.haskell.org/haskellwiki/Typeclassopedia
 * Laws: TODO
 *
 */
@TypeChecked
interface Applicative<App> extends Functor<App> {

    /**
     * pure  :: a -> f a
     */
    def <A> App<A> pure(A a)

    /**
     * (<*>) :: f (a -> b) -> f a -> f b
     */
    def <A, B> App<B> apply(App<F<A, B>> t1, App<A> t2)

}
