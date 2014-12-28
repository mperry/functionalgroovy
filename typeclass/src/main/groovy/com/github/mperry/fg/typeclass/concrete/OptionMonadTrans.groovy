package com.github.mperry.fg.typeclass.concrete

import com.github.mperry.fg.typeclass.Monad
import com.github.mperry.fg.typeclass.MonadTrans
import fj.F
import fj.data.Option
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 27/12/2014.
 */
@TypeChecked
class OptionMonadTrans extends MonadTrans<OptionT> {

    @Override
    def <A, M> OptionT<M, A> unit(M<A> m, Monad<M> monad) {
        OptionT.point(monad.map(m, { A a -> Option.some(a) } as F), monad)
    }

    @Override
    def <M, A, B> OptionT<M, B> flatMap(OptionT<M, A> t, Monad<M> m, F<A, OptionT<M, B>> f) {
        unit(m.flatMap(t.value, { Option<A> o ->
            if (o.isNone()) {
                m.unit(o.<B>none())
            } else {
                f.f(o.some()).value
            }
        } as F), m)
    }

}
