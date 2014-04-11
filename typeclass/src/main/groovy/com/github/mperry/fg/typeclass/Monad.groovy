package com.github.mperry.fg.typeclass

import fj.F
import fj.F2
import fj.F3
import fj.Unit
import fj.data.Stream
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 30/12/13.
 * @see http://hackage.haskell.org/package/base-4.7.0.0/docs/Control-Monad.html
 *
 * TODO:
 * replicateM_, mapAndUnzipM, zipWithM, zipWithM_, liftM4, liftM5
 */
@TypeChecked(TypeCheckingMode.SKIP)
//@TypeChecked
abstract class Monad<M> extends Applicative<M> {

    /**
     * Implements Functor interface using Monad combinators
     * fmap :: (a -> b) -> f a -> f b
     */
    def <A, B> M<B> fmap(F<A, B> f, M<A> ma) {
        liftM(ma, f)
    }

    /**
     * Implements Applicative.pure using Monad combinators
     * pure  :: a -> f a
     */
    def <A> M<A> pure(A a) {
        unit(a)
    }

    /**
     * Implements Applicative.apply using Monad combinators
     * (<*>) :: f (a -> b) -> f a -> f b
     */
    def <A, B> M<B> apply(M<F<A, B>> t1, M<A> t2) {
        ap(t2, t1)
    }

    /**
     * Sequentially compose two actions, passing any value produced by the first as
     * an argument to the second.
     * (>>=) :: forall a b. m a -> (a -> m b) -> m b
     */
    abstract <A, B> M<B> flatMap(M<A> ma, F<A, M<B>> f)

    /**
     * Inject a value into the monadic type.
     * return :: a -> m a
     */
    abstract <B> M<B> unit(B b)

    /**
     * Returns a function representing unit
     */
    def <B> F<B, M<B>> unit() {
        { B b ->
            unit(b)
        } as F
    }

    /**
     * The join function is the conventional monad join operator. It is used to remove
     * one level of monadic structure, projecting its bound argument into the outer level.
     * join :: Monad m => m (m a) -> m a
     */
    def <A> M<A> join(M<M<A>> mma) {
        flatMap(mma, {M<A> ma -> ma} as F)
    }

    def <A, B> M<B> map(M<A> ma, F<A, B> f) {
        flatMap(ma, { A a ->
            unit(f.f(a))
        } as F)
    }

    def <A, B, C> M<C> map2(M<A> ma, M<B> mb, F2<A, B, C> f) {
        flatMap(ma, { A a -> map(mb, { B b -> f.f(a, b)} as F)} as F)
    }

    def <A, B> M<B> to(M<A> ma, B b) {
        map(ma, { A a -> b } as F)
    }

    def <A> M<Unit> skip(M<A> ma) {
        to(ma, Unit.unit())
    }

    /**
     * The foldM function is analogous to foldl, except that its result is encapsulated
     * in a monad. Note that foldM works from left-to-right over the list arguments.
     * This could be an issue where (>>) and the `folded function' are not commutative.
     * foldM :: Monad m => (a -> b -> m a) -> a -> [b] -> m a
     * Arguments are in different order
     */
    def <A, B> M<B> foldM(Stream<A> s, B b, F2<B, A, M<B>> f) {
        if (s.empty) {
            unit(b)
        } else {
            def h = s.head()
            def t = s.tail()._1()
            def newF = { B bb -> foldM(t, bb, f)} as F
            def m = f.f(b, h)
            flatMap(m, newF)
        }
    }

    /**
     * Like foldM, but discards the result.
     * foldM_ :: Monad m => (a -> b -> m a) -> a -> [b] -> m ()
     */
    def <A, B> M<Unit> foldM_(Stream<A> s, B b, F2<B, A, M<B>> f) {
        skip(foldM(s, b, f))
    }

    /**
     * Evaluate each action in the sequence from left to right, and collect the results.
     * @param list
     * @return
     */
    def <A> M<List<A>> sequence(List<M<A>> list) {

        def k2 = { M<List<A>> acc, M<A> ma ->
            flatMap(acc, { xs ->
                map(ma, { x ->
                    xs + [x]
                } as F)
            } as F)
        }
        list.foldLeft(unit([]), k2)
    }

    /**
     * Map each element of a structure to an action, evaluate these actions from left to right
     * and collect the results.
     * @param list
     * @param f
     * @return
     */
    def <A, B> M<List<B>> traverse(List<A> list, F<A, M<B>> f) {
        (M<List<B>>) list.foldLeft(unit([]), { M<List<B>> acc, A a ->
            flatMap(acc, { bs ->
                def mb = f.f(a)
                map(mb, { b ->
                    bs + [b]
                } as F)
            } as F)
        } as F2)
    }

    /**
     * replicateM n act performs the action n times, gathering the results.
     * replicateM :: Monad m => Int -> m a -> m [a] Source
     */
    def <A> M<List<A>> replicateM(Integer n, M<A> ma) {
        sequence(List.repeat(n, ma))
    }

    /**
     * Right-to-left Kleisli composition of monads. (>=>), with the arguments flipped
     * http://hackage.haskell.org/package/base-4.6.0.1/docs/Control-Monad.html#v:-60--61--60-
     * @param l
     * @param f
     * @param g
     * @return
     */
    def <A, B, C> F<A, M<C>> compose(F<B, M<C>> f, F<A, M<B>> g) {
        { A a ->
            flatMap(g.f(a), f)
        } as F
    }

    /**
     * This generalizes the list-based filter function.
     * Arguments flipped compared to Haskell representation
     * filterM :: Monad m => (a -> m Bool) -> [a] -> m [a] Source
     */
    def <A> M<List<A>> filterM(List<A> list, F<A, M<Boolean>> f) {
        if (list.empty) {
            unit([])
        } else {
            def h = list.head()
            def mb = f.f(h)
            flatMap(mb, { Boolean b ->
                def mList = filterM(list.tail(), f)
                map(mList, { List<A> listAs ->
                    unit(b ? [h] + listAs : listAs)
                } as F)
            } as F)
        }
    }

    /**
     * Conditional execution of monadic expressions. For example,
     * when debug (putStr "Debugging\n")
     * will output the string Debugging\n if the Boolean value debug is True, and
     * otherwise do nothing.
     * when :: Monad m => Bool -> m () -> m ()
     */
    def M<Unit> when(Boolean b, M<Unit> m) {
        b ? m : unit(Unit.unit())
    }

    /**
     * The reverse of when.
     * unless :: Monad m => Bool -> m () -> m () Source
     */
    def M<Unit> unless(Boolean b, M<Unit> m) {
        when(!b, m)
    }

    /**
     * Promote a function to a monad.
     * liftM :: Monad m => (a1 -> r) -> m a1 -> m r
     */
    def <A, B> M<B> liftM(M<A> ma, F<A, B> f) {
        map(ma, { A a ->
            unit(f.f(a))
        } as F)
    }

    /**
     * Promote a function to a monad, scanning the monadic arguments from left to
     * right. For example,
     * liftM2 (+) [0,1] [0,2] = [0,2,1,3]
     * liftM2 (+) (Just 1) Nothing = Nothing
     * liftM2 :: Monad m => (a1 -> a2 -> r) -> m a1 -> m a2 -> m r
     */
    def <A, B, R> M<R> liftM2(M<A> ma, M<B> mb, F2<A, B, R> f) {
        flatMap(ma, { A a ->
            map(mb, { B b ->
                unit(f.f(a, b))
            } as F)
        } as F)
    }

    /**
     * Promote a function to a monad, scanning the monadic arguments from left to
     * right (cf. liftM2).
     * liftM3 :: Monad m => (a1 -> a2 -> a3 -> r) -> m a1 -> m a2 -> m a3 -> m r
     */
    def <A, B, C, R> M<R> liftM3(M<A> ma, M<B> mb, M<C> mc, F3<A, B, C, R> f) {
        flatMap(ma, { A a ->
            flatMap(mb, { B b ->
                map(mc, { C c ->
                    unit(f.f(a, b, c))
                } as F)
            } as F)
        } as F)
    }

    /**
     * In many situations, the liftM operations can be replaced by uses of ap, which
     * promotes function application.
     * return f `ap` x1 `ap` ... `ap` xn
     * is equivalent to
     * liftMn f x1 x2 ... xn
     * ap :: Monad m => m (a -> b) -> m a -> m b Source
     */
    def <A, B> M<B> ap(M<A> ma, M<F<A, B>> mf) {
        flatMap(mf, { F<A, B> f ->
            map(ma, { A a ->
                f.f(a)
            } as F)
        } as F)
    }

}
