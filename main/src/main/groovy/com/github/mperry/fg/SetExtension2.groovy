package com.github.mperry.fg

import com.github.mperry.fg.typeclass.Monad
import com.github.mperry.fg.typeclass.concrete.SetMonad
import fj.F
import fj.F2
import fj.F3
import fj.Unit
import fj.data.Stream
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 12/04/2014.
 */
@TypeChecked
class SetExtension2 {

    static <A> Set<A> create() {
        new HashSet<A>()
    }

    static <A> SetMonad<A> monad() {
        new SetMonad<A>()
    }

    static <A, B> Set<B> fmap(Set<A> ma, F<A, B> f) {
        this.<A>monad().fmap(f, ma)
    }


    static <A, B> Set<B> apply(Set<A> sa, Set<F<A, B>> sf) {
		this.<A>monad().apply(sf, sa)
    }

    static <A, B> Set<B> flatMap(Set<A> ma, F<A, Set<B>> f){
		this.<A>monad().flatMap(ma, f)
    }

    static <A, B> Set<B> map(Set<A> ma, F<A, B> f) {
        def result = this.<B>create()
        for (A a: ma) {
            result.add(f.f(a))
        }
        result
    }

    static <A, B, C> Set<C> map2(Set<A> ma, Set<B> mb, F2<A, B, C> f) {
		this.<A>monad().map2(ma, mb, f)
    }

    static <A, B> Set<B> to(Set<A> ma, B b) {
		this.<A>monad().to(ma, b)
    }

    static <A> Set<Unit> skip(Set<A> ma) {
		this.<A>monad().skip(ma)
    }

    static <A> Set<List<A>> replicateM(Set<A> ma, Integer n) {
		this.<A>monad().replicateM(n, ma)
    }

    static <A, B> Set<B> liftM(Set<A> ma, F<A, B> f) {
		this.<A>monad().liftM(ma, f)
    }

    static <A, B, R> Set<R> liftM2(Set<A> ma, Set<B> mb, F2<A, B, R> f) {
		this.<A>monad().liftM2(ma, mb, f)
    }

    static <A, B, C, R> Set<R> liftM3(Set<A> ma, Set<B> mb, Set<C> mc, F3<A, B, C, R> f) {
		this.<A>monad().liftM3(ma, mb, mc, f)
    }

    static <A, B> Set<B> ap(Set<A> ma, Set<F<A, B>> mf) {
		this.<A>monad().ap(ma, mf)
    }

}
