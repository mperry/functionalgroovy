package com.github.mperry.fg.typeclass.concrete

import com.github.mperry.fg.typeclass.Monad
import fj.F
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 10/04/2014.
 */
@TypeChecked
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
    def <A> Set<A> unit(A b) {
        def result = this.<A>defaultSet()
        result.add(b)
        result
    }

}
