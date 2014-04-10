package com.github.mperry.fg.typeclass.concrete

import com.github.mperry.fg.typeclass.Functor
import fj.F
import fj.data.Option

/**
 * Created by MarkPerry on 10/04/2014.
 */
class OptionFunctor implements Functor<Option> {

    @Override
    def <A, B> Option<B> fmap(F<A, B> f, Option<A> fa) {
        fa.map(f)
    }
}
