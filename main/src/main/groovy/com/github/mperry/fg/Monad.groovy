package com.github.mperry.fg

import fj.F
import fj.F2
import fj.Unit
import fj.data.Stream
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

//import static com.github.mperry.fg.Comprehension.foreach

/**
 * Created by MarkPerry on 30/12/13.
 */
//@TypeChecked
abstract class Monad<M> {

    abstract <A, B> M<B> flatMap(M<A> ma, F<A, M<B>> f)

    abstract <B> M<B> unit(B b)

    def <B> F<B, M<B>> unit() {
        { B b ->
            unit(b)
        } as F
    }

    def <A> M<A> join(M<M<A>> mma) {
//    def <A> M<A> join(M<A> mma) {
        flatMap(mma, {M<A> ma -> ma} as F)
    }

    def <A, B> M<B> map(M<A> ma, F<A, B> f) {
        flatMap(ma, { A a -> unit(f.f(a)) } as F)
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


    def <A> M<List<A>> sequence(List<M<A>> list) {

        def k2 = { M<List<A>> acc, M<A> ma ->
            acc.flatMap { xs ->
                ma.map { x ->
                    xs + [x]
//                    [x] + xs
                }
            }
        }
        list.foldLeft(unit([]), k2)

    }

//    def <A, B> M<List<B>> traverse(List<A> list, F f) {
    def <A, B> M<List<B>> traverse(List<A> list, F<A, M<B>> f) {
        (M<List<B>>) list.foldLeft(unit([]), { M<List<B>> acc, A a ->
            acc.flatMap { bs ->
                def mb = f.f(a)
                mb.map { b ->
                    bs + [b]
                }
            }

        } as F2)
    }

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

}
