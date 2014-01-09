package com.github.mperry.fg

import fj.F
import fj.F2
import fj.Unit
import fj.data.Stream
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 9/01/14.
 */
@TypeChecked
abstract class MonadLike<A> {

    def abstract <B, C> MonadLike<C> flatMap(F<A, MonadLike<C>> f)

    def abstract <B> MonadLike<B> unit(B b)

    def <B> F<B, MonadLike<B>> unit() {
        { B b ->
            unit(b)
        } as F
    }

    def <B> MonadLike<B> join(MonadLike<MonadLike<B>> mmb) {
        mmb.flatMap({ MonadLike<B> mb -> mb } as F)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <B, C> MonadLike<C> map(F<A, C> f) {
        flatMap({ A a -> unit(f.f(a)) } as F)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <B, C, D> MonadLike<C> map2(MonadLike<C> mc, F2<A, C, D> f) {
        flatMap({ A a -> mc.map({ C c -> f.f(a, c) } as F) } as F)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <B, C> MonadLike<C> as_(C c) {
        map({ A a -> c } as F)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <B> MonadLike<Unit> skip() {
        as_(Unit.unit())
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <B, C> MonadLike<C> foldM(Stream<B> s, C c, F2<C, B, MonadLike<C>> f) {
        if (s.empty) {
            unit(c)
        } else {
            def h = s.head()
            def t = s.tail()._1()
            flatMap(f.f(c, h), { C cc -> foldM(t, cc, f) } as F)
        }
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <B, C> MonadLike<Unit> foldM_(Stream<B> s, C c, F2<C, B, MonadLike<C>> f) {
        skip(foldM(s, c, f))
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <B> MonadLike<List<B>> sequence(List<MonadLike<B>> list) {
        (MonadLike<List<B>>) list.inject(unit([])) { MonadLike<List<B>> acc, MonadLike<B> mb ->
            mb.map2(acc, { B b, List<B> lbs -> [b] + lbs } as F2)
        }
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <B, C> MonadLike<List<C>> traverse(List<B> list, F<B, MonadLike<C>> f) {
        (MonadLike<List<C>>) list.inject(unit([])) { MonadLike<List<C>> acc, B b ->
            f.f(b).map2(acc, { C c, List<C> lc -> [c] + lc } as F2)
        }
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <B> MonadLike<List<B>> replicateM(Integer n, MonadLike<B> mb) {
        sequence(List.repeat(n, mb))
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <B, C, D> F<B, MonadLike<D>> compose(F<B, MonadLike<C>> f, F<C, MonadLike<D>> g) {
        { B b -> f.f(b).flatMap(g) } as F
    }


}
