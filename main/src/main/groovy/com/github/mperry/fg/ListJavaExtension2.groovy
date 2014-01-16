package com.github.mperry.fg

import fj.F
import fj.F2
import fj.Unit

//import groovy.transform.TypeChecked
//import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 16/01/14.
 */
//@TypeChecked
class ListJavaExtension2 {

    static ListMonad monad() {
        new ListMonad()
    }

    static <A, B, C> List<A> map2(List<A> listA, List<B> listB, F2<A, B, C> f) {
        monad().map2(listA, listB, f)
    }

    static <A, B> List<B> as_(List<A> list, B b) {
        monad().as_(list, b)
    }

    static <A> List<Unit> skip(List<A> list) {
        monad().skip(list)
    }

    static <A> List<List<A>> sequence(List<List<A>> list) {
        monad().sequence(list)
    }

    static <A, B> List<List<B>> traverse(List<A> list, F<A, List<B>> f) {
        monad().traverse(list, f)
    }

    static <A, B> List<List<B>> replicateM(List<A> list, Integer n) {
        monad().replicateM(n, list)
    }



}
