package com.github.mperry.fg.typeclass.concrete

import com.github.mperry.fg.typeclass.Monad
import fj.F

/**
 * Created by MarkPerry on 10/04/2014.
 */
class SetMonad extends Monad<Set> {

    static <B> Set<B> defaultSet() {
        new HashSet<B>()
    }

    @Override
    def <A, B> Set<B> flatMap(Set<A> ma, F<A, Set<B>> f) {
        def result = this.<B>defaultSet()
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
