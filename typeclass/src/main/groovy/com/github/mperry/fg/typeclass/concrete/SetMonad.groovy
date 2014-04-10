package com.github.mperry.fg.typeclass.concrete

import com.github.mperry.fg.typeclass.Monad
import fj.F

/**
 * Created by MarkPerry on 10/04/2014.
 */
class SetMonad<A> extends Monad<Set<A>> {

    static <B> Set<B> defaultSet() {
        new TreeSet<B>()
    }

    @Override
    def <A, B> Set<A> flatMap(Set<A> ma, F<A, Set<A>> f) {
        def result = this.<A>defaultSet()
        for (A a: ma) {
            result.addAll(f.f(a))
        }
        result
    }

    @Override
    def <B> Set<B> unit(B b) {
        def result = this.<B>defaultSet()
        result.add(b)
        result
    }

}
