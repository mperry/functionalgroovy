package com.github.mperry.fg.typeclass.trait

import com.github.mperry.fg.Functions
import fj.F
import fj.F2
import fj.F3
import fj.Function
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 9/04/2014.
 * @see http://www.haskell.org/haskellwiki/Typeclassopedia
 * @see http://hackage.haskell.org/package/base-4.2.0.0/docs/Control-Applicative.html
 *
 * Laws:
 * identity: pure id <*> v = v
 * composition: pure (.) <*> u <*> v <*> w = u <*> (v <*> w)
 * homomorphism: pure f <*> pure x = pure (f x)
 * interchange: u <*> pure y = pure ($ y) <*> u
 * ignore left value: u *> v = pure (const id) <*> u <*> v
 * ignore right value: u <* v = pure const <*> u <*> v
 */
@TypeChecked
trait ApplicativeT<App> extends FunctorT<App> {

    /**
     * pure  :: a -> f a
     */
    abstract <A> App<A> pure(A a)

    /**
     * (<*>) :: f (a -> b) -> f a -> f b
     */
    abstract <A, B> App<B> apply(App<F<A, B>> t1, App<A> t2)

    /**
     * (<*) :: f a -> f b -> f a
     */
    def <A, B> App<A> left(App<A> a1, App<B> a2) {
        a1
    }

    /**
     * (*>) :: f a -> f b -> f b
     */
    def <A, B> App<B> right(App<A> a1, App<B> a2) {
        a2
    }

    /**
     * liftA :: Applicative f => (a -> b) -> f a -> f b
     */
    def <A, B> App<B> liftA(F<A, B> f, App<A> a1) {
        apply(pure(f), a1)
    }

    /**
     * liftA2 :: Applicative f => (a -> b -> c) -> f a -> f b -> f c
     */
    @TypeChecked(TypeCheckingMode.SKIP)
    def <A, B, C> App<C> liftA2(F2<A, B, C> f, App<A> apa, App<B> apb) {
        apply(fmap(Functions.curry(f), apa), apb)
    }

    /**
     * liftA3 :: Applicative f => (a -> b -> c -> d) -> f a -> f b -> f c -> f d
     */
    @TypeChecked(TypeCheckingMode.SKIP)
    def <A, B, C, D> App<D> liftA3(F3<A, B, C, D> f, App<A> apa, App<B> apb, App<C> apc) {
        apply(apply(fmap(Function.curry(f), apa), apb), apc)
    }

}
