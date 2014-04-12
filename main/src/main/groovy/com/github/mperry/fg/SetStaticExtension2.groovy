package com.github.mperry.fg

import com.github.mperry.fg.typeclass.Monad
import com.github.mperry.fg.typeclass.concrete.SetMonad
import fj.F
import fj.F2
import fj.Unit
import fj.data.Stream
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 12/04/2014.
 */
@TypeChecked
class SetStaticExtension2 {

    static Monad monad() {
        new SetMonad()
    }

    static <A> Set<A> pure(Set z, A a) {
        monad().pure(a)
    }

    static <B> Set<B> unit(Set z, B b) {
        monad().unit(b)
    }


    static <B> F<B, Set<B>> unit(Set z) {
        monad().unit()
    }

    static <A> Set<A> join(Set z, Set<Set<A>> mma) {
        monad().join(mma)
    }


    static <A, B> Set<B> foldM(Set z, List<A> list, B b, F2<B, A, Set<B>> f) {
        monad().foldM(list, b, f)
    }

    static <A, B> Set<Unit> foldM_(Set z, Stream<A> s, B b, F2<B, A, Set<B>> f) {
        monad().foldM_(s, b, f)
    }

    static <A, B> Set<Unit> foldM_(Set z, List<A> s, B b, F2<B, A, Set<B>> f) {
        monad().foldM_(s, b, f)
    }

    static <A> Set<List<A>> sequence(Set z, List<Set<A>> list) {
        monad().sequence(list)
    }

    static <A, B> Set<List<B>> traverse(Set z, List<A> list, F<A, Set<B>> f) {
        monad().traverse(list, f)
    }

    static <A, B, C> F<A, Set<C>> compose(Set z, F<B, Set<C>> f, F<A, Set<B>> g) {
        monad().compose(f, g)
    }

    static <A> Set<List<A>> filterM(Set z, List<A> list, F<A, Set<Boolean>> f) {
        monad().filterM(list, f)
    }



}
