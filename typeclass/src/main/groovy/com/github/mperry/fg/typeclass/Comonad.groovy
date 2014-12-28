package com.github.mperry.fg.typeclass

import fj.F
import fj.F1Functions
import fj.F2
import fj.F2Functions
import groovy.transform.TypeChecked


import static fj.F2Functions.*

/**
 * Created by MarkPerry on 28/12/2014.
 */
@TypeChecked
abstract class Comonad<CM> implements Functor<CM> {

    def <A, B> CM<B> map(CM<A> fa, F<A, B> f)  {
        liftW(fa, f)
    }

    /**
     * extract :: w a -> a
     */
    abstract <A> A extract(CM<A> c);

    /**
     * duplicate :: w a -> w (w a)
     */
    def <A> CM<CM<A>> duplicate(CM<A> c) {
        def id = { A a -> a } as F
        flip(extend()).f(id, c)
    }

    /**
     * extend :: (w a -> b) -> w a -> w b
     */
    def <A, B> CM<B> extend (CM<A> c, F<CM<A>, B> f) {
        this.map(duplicate(c), f)
    }

    def <A, B> F2<CM<A>, F<CM<A>, B>, CM<B>> extend() {
        { CM<A> cm, F<CM<A>, B> f -> extend(cm, f) } as F2
    }

    /**
     * liftW :: Comonad w => (a -> b) -> w a -> w b
     */
    def <A, B> CM<B> liftW(CM<A> cm, F<A, B> f) {
        extend(cm, { CM<A> cma -> f.f(this.<A>extract(cma)) } as F)
    }

    /**
     * wfix :: Comonad w => w (w a -> a) -> a
     * wfix w = extract w (extend wfix w)
     */
    def <A> A wfix(CM<F<CM<A>, A>> c) {
        extract(extend(c, wfix()))
    }

    def <A> F<CM<F<CM<A>, A>>, A> wfix() {
        { CM<F<CM<A>, A>> cm -> wfix(cm) } as F
    }

    /*
    def <A> CM<A> cfix(F<CM<A>, A> f) {
        def result = F2Functions.f(flip(extend()), f)
        // TODO: call fix(result)
        // hard to do: implement at some future point
        throw new UnsupportedOperationException("Yet to be implemented")
    }
    */

    /**
     * (=>=) :: Comonad w => (w a -> b) -> (w b -> c) -> w a -> c
     * Left-to-right Cokleisli composition
     */
    def <A, B, C> C rightShift(F<CM<A>,B> f, F<CM<B>, C> g, CM<A> cm) {
        F1Functions.o(g, F2Functions.f(flip(extend()), f))
    }

    /**
     * (=<=) :: Comonad w => (w b -> c) -> (w a -> b) -> w a -> c
     * Right-to-left Cokleisli composition
     */
    def <A, B, C> C leftShift(F<CM<B>, C> f, F<CM<A>, B> g, CM<A> cm) {
        F1Functions.o(f, F2Functions.f(flip(extend()), g))
    }





}
