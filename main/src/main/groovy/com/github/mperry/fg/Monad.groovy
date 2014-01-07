package com.github.mperry.fg

import fj.F
import fj.F2
import fj.Unit
import fj.data.Stream
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 30/12/13.
 */
@TypeChecked
abstract class Monad<M> {

    abstract <A, B> M<B> flatMap(M<A> ma, F<A, M<B>> f)

    abstract <B> M<B> unit(B b)

    def <B> F<B, M<B>> unit() {
        { B b ->
            unit(b)
        } as F
    }

    def <A> M<A> join(M<M<A>> mma) {
        flatMap(mma, {M<A> ma -> ma} as F)
    }

    def <A, B> M<B> map(M<A> ma, F<A, B> f) {
        flatMap(ma, { A a -> unit(f.f(a)) } as F)
    }

    def <A, B, C> M<C> map2(M<A> ma, M<B> mb, F2<A, B, C> f) {
        flatMap(ma, { A a -> map(mb, { B b -> f.f(a, b)} as F)} as F)
    }

    def <A, B> M<B> as_(M<A> ma, B b) {
        map(ma, { A a -> b } as F)
    }

    def <A> M<Unit> skip(M<A> ma) {
        as_(ma, Unit.unit())
    }

    def <A, B> M<B> foldM(Stream<A> s, B b, F2<B, A, M<B>> f) {
        if (s.empty) {
            unit(b)
        } else {
            def h = s.head()
            def t = s.tail()._1()
            flatMap(f.f(b, h), {B bb -> foldM(t, bb, f)} as F)
        }
    }

    def <A, B> M<Unit> foldM_(Stream<A> s, B b, F2<B, A, M<B>> f) {
        skip(foldM(s, b, f))
    }

    def <A> M<List<A>> sequence(List<M<A>> list) {
        (M<List<A>>) list.inject(unit([])) { M<List<A>> acc, M<A> ma ->
            map2(ma, acc, { A a, List<A> las -> [a] + las } as F2)
        }
    }

    def <A, B> M<List<B>> traverse(List<A> list, F<A, M<B>> f) {
        (M<List<B>>) list.inject(unit([])) { M<List<B>> acc, A a ->
            map2(f.f(a), acc, { B b, List<B> lb -> [b] + lb } as F2)
        }
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <A> M<List<A>> replicateM(Integer n, M<A> ma) {
        sequence(List.repeat(n, ma))
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <A, B, C> F<A, M<C>> compose(F<A, M<B>> f, F<B, M<C>> g) {
        { A a -> flatMap(f.f(a), g)} as F
    }

}
