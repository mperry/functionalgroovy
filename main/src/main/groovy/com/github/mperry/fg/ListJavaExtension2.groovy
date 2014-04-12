package com.github.mperry.fg

import com.github.mperry.fg.typeclass.concrete.ListApplicative
import com.github.mperry.fg.typeclass.concrete.ListFunctor
import com.github.mperry.fg.typeclass.concrete.ListMonad
import fj.F
import fj.F2
import fj.F2Functions
import fj.F3
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

    static <A, B> List<B> ap(List<A> ma, List<F<A, B>> mf) {
        monad().ap(ma, mf)
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

    static <A> List<List<A>> filterM(List<A> list, F<A, List<Boolean>> f) {
        monad().filterM(list, f)
    }

    static <A, B> List<B> liftM(List<A> ma, F<A, B> f) {
        monad().liftM(ma, f)
    }

    static <A, B, R> List<R> liftM2(List<A> ma, List<B> mb, F2<A, B, R> f) {
        monad().liftM2(ma, mb, f)
    }

    static <A, B, C, R> List<R> liftM3(List<A> ma, List<B> mb, List<C> mc, F3<A, B, C, R> f) {
        monad().liftM3(ma, mb, mc, f)
    }

    // Applicative

    static ListApplicative applicative() {
        new ListApplicative()
    }

    static <A, B> List<B> apply(List<A> list, List<F<A, B>> listFs) {
        applicative().apply(listFs, list)
    }

    static <A, B> List<B> liftA(List<A> a1, F<A, B> f) {
        applicative().liftA(f, a1)
    }


    static <A, B, C> List<C> liftA2(List<A> listAs, List<B> listBs, F2<A, B, C> f) {
        applicative().liftA2(f, listAs, listBs)
    }

    static <A, B, C, D> List<D> liftA3(List<A> apa, List<B> apb, List<C> apc, F3<A, B, C, D> f) {
        applicative().liftA3(f, apa, apb, apc)
    }

}
