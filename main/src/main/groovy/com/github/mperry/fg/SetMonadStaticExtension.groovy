package com.github.mperry.fg

import com.github.mperry.fg.typeclass.concrete.ListMonad
import com.github.mperry.fg.typeclass.concrete.SetMonad
import fj.F
import fj.F2
import fj.Unit
import fj.data.Stream
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 13/04/2014.
 */
@TypeChecked
class SetMonadStaticExtension {

    static SetMonad monad() {
        new SetMonad()
    }

    static <A> Set<A> join(Set list, Set<Set<A>> list2) {
        monad().join(list2)
    }

    static <A> Set<List<A>> filterM(Set z, List<A> list, F<A, Set<Boolean>> f) {
        monad().filterM(list, f)
    }

    static <A, B> Set<B> foldM(Set l, Stream<A> s, B b, F2<B, A, Set<B>> f) {
        monad().foldM(s, b, f)
    }

    static <A, B> Set<Unit> foldM_(Set l, Stream<A> s, B b, F2<B, A, Set<B>> f) {
        monad().foldM_(s, b, f)
    }

    static <A> Set<List<A>> sequence(Set l, List<Set<A>> list) {
        monad().sequence(list)
    }

    static <A, B, C> F<A, Set<C>> compose(Set l, F<B, Set<C>> f,  F<A, Set<B>> g) {
        monad().compose(f, g)
    }


}
