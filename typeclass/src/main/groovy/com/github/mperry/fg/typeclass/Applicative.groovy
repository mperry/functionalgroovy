package com.github.mperry.fg.typeclass

//import com.github.mperry.fg.Functions
import com.github.mperry.fg.ListOps
import fj.F
import fj.F1Functions
import fj.F2
import fj.F3
import fj.F3Functions
import fj.Function
import groovy.transform.TypeChecked

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
abstract class Applicative<App> implements Functor<App> {

    /**
     * lift value into Applicative
     * pure  :: a -> f a
     */
    abstract <A> App<A> pure(A a)

    /**
     * Sequence computations and combine their results
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
    def <A, B, C> App<C> liftA2(F2<A, B, C> f, App<A> apa, App<B> apb) {
        apply(map(apa, Function.curry(f)), apb)
    }

    def <A, B, C> F3<F2<A, B, C>, App<A>, App<B>, App<C>> liftA2_() {
        { F2<A, B, C> f2, App<A> a, App<B> b ->
            liftA2(f2, a, b)
        } as F3
    }

    /**
     * liftA3 :: Applicative f => (a -> b -> c -> d) -> f a -> f b -> f c -> f d
     */
    def <A, B, C, D> App<D> liftA3(F3<A, B, C, D> f, App<A> apa, App<B> apb, App<C> apc) {
        apply(apply(map(apa, Function.curry(f)), apb), apc)
    }

    def <A> App<List<A>> sequenceA(List<App<A>> list) {
        def cons = {
            A a, List<A> listAs -> ListOps.plus(a, listAs)
        } as F2
//        def cons_ = {
//            A a ->
//                { List<A> listAs -> ListOps.plus(a, listAs) } as F
//        } as F
        def f2 = F3Functions.f(liftA2_(), cons)
        list.foldRight(pure([]), f2)
    }

}
