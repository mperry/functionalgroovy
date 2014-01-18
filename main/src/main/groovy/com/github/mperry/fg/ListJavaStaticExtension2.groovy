package com.github.mperry.fg

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

//    static ListMonad monad() {
//        new ListMonad()
//    }

    static <A> List<A> join(List list, List<List<A>> list2) {
        new ListMonad().join(list2)
    }

    static <A, B, C> List<C> map2(List l, List<A> listA, List<B> listB, F2<A, B, C> f) {
        new ListMonad().map2(listA, listB, f)
    }

    static <A, B> List<A> foldM(List l, Stream<A> s, B b, F2<B, A, List<B>> f) {
        new ListMonad().foldM(s, b, f)
    }

    static <A, B> List<Unit> foldM_(List l, Stream<A> s, B b, F2<B, A, List<B>> f) {
        new ListMonad().foldM_(s, b, f)
    }

    static <A> List<List<A>> sequence(List l, List<List<A>> list) {
        new ListMonad().sequence(list)
    }

    static <A, B, C> F<A, List<C>> compose(List l, F<B, List<C>> f,  F<A, List<B>> g) {
        new ListMonad().compose(f, g)
    }

}
