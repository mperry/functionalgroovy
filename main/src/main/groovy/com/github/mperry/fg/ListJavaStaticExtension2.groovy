package com.github.mperry.fg

import fj.F
import fj.F2
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

    static <A, B> List<A> foldM(List l, Stream<A> s, B b, F2<B, A, List<B>> f) {
        new ListMonad().foldM(s, b, f)
    }

    static <A, B> List<A> foldM_(List l, Stream<A> s, B b, F2<B, A, List<B>> f) {
        new ListMonad().foldM_(s, b, f)
    }

    static <A, B, C> F<A, List<C>> compose(List l, F<A, List<B>> f, F<B, List<C>> g) {
        new ListMonad().compose(f, g)
    }

}
