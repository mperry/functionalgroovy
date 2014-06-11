package com.github.mperry.fg

import groovy.transform.TypeChecked

import java.util.function.BiFunction
import java.util.function.Function
import fj.data.*

/**
 * Created by mperry on 10/06/2014.
 */
@TypeChecked
class OptionalExtension {

    /**
     * Performs function application within an optional value (applicative functor pattern).
     */
    static <A, B> Optional<B> apply(Optional<A> o1, Optional<Function<A, B>> o2) {
        Function<Function<A, B>, Optional<B> > f = { Function<A, B> f2 -> o1.map(f2) } as Function<Function, Optional>
        o2.flatMap(f)
    }

    /**
     * Returns <code>true</code> is this optional value has a value and the given predicate function
     * holds on that value, <code>false</code> otherwise.
     */
    static <A> Boolean exists(Optional<A> o1, Function<A, Boolean> f) {
        o1.isPresent() && f.apply(o1.get())
    }

    static <A> Boolean forall(Optional<A> o1, Function<A, Boolean> f) {
        !o1.isPresent() || f.apply(o1.get())
    }

    static <A> void foreach(Optional<A> o1, Function<A, Void> f) {
        if (o1.isPresent()) {
            f.apply(o1.get())
        }
    }

    static <A, B, C> Optional<C> liftM2(Optional<A> o1, Optional<B> o2, BiFunction<A, B, C> f) {
        o1.flatMap { A a ->
            o2.map { B b ->
                f.apply(a, b)
            }
        }
    }

    static <A, B> B orElseGet(Optional<A> o1, B b, Function<A, B> f) {
        o1.isPresent() ? f.apply(o1.get()) : b
    }

    /**
     * Returns the value from this optional value, or if there is no value, returns <code>null</code>.
     * This is intended for interfacing with APIs that expect a <code>null</code> for non-existence.
     *
     * @return This optional value or <code>null</code> if there is no value.
     */
   static <A> A toNull(Optional<A> o1) {
       o1.orElse(null)
   }

}
