package com.github.mperry.fg

import fj.F
import fj.F1Functions
import fj.P1
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 29/11/2014.
 */
@TypeChecked
abstract class Free<K, A> {

    def <B> Free<K, B> flatMap(F<A, Free<K, B>> f) {
        new FlatMap<K, A, B>(this, f)
    }

    def <B> Free<K, B> map(F<A, B> f) {
        flatMap(F1Functions.andThen(f, { B b -> new Return<K, B>(b) } ))
    }

    Free<K, A> step(Free<K, A> f) {
        f
    }



    static <A> A runTrampoline(Free<P1<A>, A> free) {


    }
}
