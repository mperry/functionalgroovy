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
 *
 * @see The Haskell Control.Monad module at http://hackage.haskell.org/package/base-4.6.0.1/docs/Control-Monad.html
 */
@TypeChecked(TypeCheckingMode.SKIP)
abstract class Monad<M> extends Applicative<M> {

    def <A> M<A> pure(A a) {
        unit(a)
    }

    def <A, B> M<B> apply(M<F<A, B>> t1, M<A> t2) {
        ap(t2, t1)
    }

    def <A, B> M<B> fmap(F<A, B> f, M<A> ma) {
        liftM(ma, f)
    }

    /**
     * Sequentially compose two actions, passing any value produced by the first as an argument to the second.
     * @param ma
     * @param f
     * @return
     */
    abstract <A, B> M<B> flatMap(M<A> ma, F<A, M<B>> f)

    /**
     * Inject a value into the monadic type.
     * @param b
     * @return
     */
    abstract <B> M<B> unit(B b)

    def <B> F<B, M<B>> unit() {
        { B b ->
            unit(b)
        } as F
    }

    /**
     * The join function is the conventional monad join operator. It is used to remove one level of
     * monadic structure, projecting its bound argument into the outer level.
     * @param mma
     * @return
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
     * The foldM function is analogous to foldl, except that its result is encapsulated in a monad. Note
     * that foldM works from left-to-right over the list arguments.
     * @param s
     * @param b
     * @param f
     * @return
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
     * performs the action n times, gathering the results.
     * @param n
     * @param ma
     * @return
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

    def <A> M<List<A>> filterM(List<A> list, F<A, M<Boolean>> f) {
        if (list.empty) {
            unit([])
        } else {
//            list.foldLeft([], { } as F2)
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

    def M<Unit> when(Boolean b, M<Unit> m) {
        b ? m : unit(Unit.unit())
    }

    def M<Unit> unless(Boolean b, M<Unit> m) {
        when(!b, m)
    }

    def <A, R> M<R> liftM(M<A> ma, F<A, R> f) {
        map(ma, { A a ->
            unit(f.f(a))
        } as F)
    }

    def <A, B, R> M<R> liftM2(M<A> ma, M<B> mb, F2<A, B, R> f) {
        flatMap(ma, { A a ->
            map(mb, { B b ->
                unit(f.f(a, b))
            } as F)
        } as F)
    }

    def <A, B, C, R> M<R> liftM3(M<A> ma, M<B> mb, M<C> mc, F3<A, B, C, R> f) {
        flatMap(ma, { A a ->
            flatMap(mb, { B b ->
                map(mc, { C c ->
                    unit(f.f(a, b, c))
                } as F)
            } as F)
        } as F)
    }

    def <A, B> M<B> ap(M<A> ma, M<F<A, B>> mf) {
        flatMap(mf, { F<A, B> f ->
            map(ma, { A a ->
                f.f(a)
            } as F)
        } as F)
    }

}
