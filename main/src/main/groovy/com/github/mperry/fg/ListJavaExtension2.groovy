package com.github.mperry.fg

import fj.F
import fj.Unit
import groovy.transform.TypeChecked

//import groovy.transform.TypeChecked
//import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 16/01/14.
 */
@TypeChecked
class ListJavaExtension2 {

//    static ListMonad2 monad() {
//        new ListMonad2()
//    }


    static <A, B> List<B> to(List<A> list, B b) {
        new ListMonad2().to(list, b)
    }

    static <A> List<Unit> skip(List<A> list) {
        new ListMonad2().skip(list)
    }

    static <A, B> List<List<B>> traverse(List<A> list, F<A, List<B>> f) {
        new ListMonad2().traverse(list, f)
    }

    static <A, B> List<List<B>> replicateM(List<A> list, Integer n) {
        new ListMonad2().replicateM(n, list)
    }



}
