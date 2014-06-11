package com.github.mperry.fg

import fj.F
import fj.F3
import fj.data.Java8
import fj.data.Option
import groovy.transform.TypeChecked

import java.util.function.BiFunction
import java.util.function.Function

import static fj.data.List.cons_

/**
 * Created by mperry on 10/06/2014.
 */
@TypeChecked
class OptionalStaticExtension {

    static <A> Optional<A> join(Optional z, Optional<Optional<A>> o) {
        def id = Function.<Optional<A>>identity()
        o.flatMap(id)
    }

    static <A> Function<Optional<Optional<A>>, Optional<A>> join(Optional z) {
        { Optional<Optional<A>> o1 ->
            join(z, o1)
        } as Function
    }

    static <A, B, C> F3<Optional<A>, Optional<B>, BiFunction<A, B, C>, Optional<C>> liftM2(Optional z) {
        { Optional<A> o1, Optional<B> o2, BiFunction<A, B, C> f ->
            OptionalExtension.liftM2(o1, o2, f)
        } as F3
    }


    static <A, B, C> Optional<List<A>> sequence(Optional z, List<Optional<A>> list) {
        Function<List<A>, List<A>> cons = { List<A> list2 -> }
        list.isEmpty() ? Optional.of([]) :
            list.head().flatMap { A a ->
                sequence(z, list.tail()).map(Java8.F_Function(ListJavaExtension.cons(a)))

            }
    }

    static <A> List<A> ofs(Optional z, List<Optional<A>> list) {
        (list.filter { Optional<A> o -> o.isPresent() }) as List<A>
    }

}
