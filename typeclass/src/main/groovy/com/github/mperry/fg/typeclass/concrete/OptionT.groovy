package com.github.mperry.fg.typeclass.concrete

import com.github.mperry.fg.typeclass.Functor
import com.github.mperry.fg.typeclass.Monad
import fj.F
import fj.data.Option
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 27/12/2014.
 */
@TypeChecked
class OptionT<M, A> {

    M<Option<A>> value

    OptionT(M<Option<A>> v) {
        value = v
    }

    static <A, M> OptionT<M, A> point(M<Option<A>> m) {
        new OptionT<M, A>(m)
    }

    def <B> OptionT<M, B> map(Functor<M> functor, F<A, B> f) {
        point(functor.map(value, { Option<A> o -> o.map(f)} as F))
    }

    def <B> OptionT<M, B> flatMap(Monad<M> monad, F<A, OptionT<M, B>> f) {
        point(monad.flatMap(value, { Option<A> o ->
            if (o.isNone()) {
                monad.unit(o.<B>none())
            } else {
                f.f(o.some()).value
            }
        } as F))
    }

}
