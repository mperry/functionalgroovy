package com.github.mperry.fg

import com.github.mperry.fg.typeclass.concrete.ListMonad
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

    static ListMonad monad() {
        new ListMonad()
    }


    static <A, B> List<B> to(List<A> list, B b) {
        monad().to(list, b)
    }

    static <A> List<Unit> skip(List<A> list) {
        monad().skip(list)
    }

    static <A, B> List<List<B>> traverse(List<A> list, F<A, List<B>> f) {
        monad().traverse(list, f)
    }

    static <A, B> List<List<B>> replicateM(List<A> list, Integer n) {
        monad().replicateM(n, list)
    }



}
