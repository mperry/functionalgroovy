package com.github.mperry.fg

import com.github.mperry.fg.typeclass.concrete.ListMonad
import fj.F
import fj.F2
import fj.Unit
import fj.data.Stream
import groovy.transform.TypeChecked

//import groovy.transform.TypeChecked
//import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 16/01/14.
 */
@TypeChecked
class ListJavaStaticExtension2 {

    static ListMonad monad() {
        new ListMonad()
    }

    static <A> List<A> join(List list, List<List<A>> list2) {
        monad().join(list2)
    }


    static <A> List<List<A>> filterM(List z, List<A> list, F<A, List<Boolean>> f) {
        monad().filterM(list, f)
    }

    static <A, B> List<B> foldM(List l, Stream<A> s, B b, F2<B, A, List<B>> f) {
        monad().foldM(s, b, f)
    }

    static <A, B> List<Unit> foldM_(List l, Stream<A> s, B b, F2<B, A, List<B>> f) {
        monad().foldM_(s, b, f)
    }

    static <A, B> List<B> foldM(List z, List<A> s, B b, F2<B, A, List<B>> f) {
        monad().foldM(s, b, f)
    }

    static <A, B> List<Unit> foldM_(List l, List<A> s, B b, F2<B, A, List<B>> f) {
        monad().foldM_(s, b, f)
    }

    static <A> List<List<A>> sequence(List l, List<List<A>> list) {
        monad().sequence(list)
    }

    static <A, B, C> F<A, List<C>> compose(List l, F<B, List<C>> f,  F<A, List<B>> g) {
        monad().compose(f, g)
    }

}
