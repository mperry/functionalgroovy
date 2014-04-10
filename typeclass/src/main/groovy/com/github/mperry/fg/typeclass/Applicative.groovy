package com.github.mperry.fg.typeclass

import fj.F
import fj.F2
import fj.F2Functions
import fj.F3
import fj.F3Functions
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 9/04/2014.
 * @see http://www.haskell.org/haskellwiki/Typeclassopedia
 * Laws: TODO
 *
 */
@TypeChecked
abstract class Applicative<App> implements Functor<App> {

    /**
     * pure  :: a -> f a
     */
    abstract <A> App<A> pure(A a)

    /**
     * (<*>) :: f (a -> b) -> f a -> f b
     */
    abstract <A, B> App<B> apply(App<F<A, B>> t1, App<A> t2)


    def <A, B> App<A> left(App<A> a1, App<B> a2) {
        a1
    }

    def <A, B> App<B> right(App<A> a1, App<B> a2) {
        a2
    }

    def <A, B> App<B> liftA(F<A, B> f, App<A> a1) {
        apply(pure(f), a1)
    }

    def <A, B, C> App<C> liftA2(F2<A, B, C> f, App<A> apa, App<B> apb) {
        apply(fmap(F2Functions.curry(f), apa), apb)
    }


}
