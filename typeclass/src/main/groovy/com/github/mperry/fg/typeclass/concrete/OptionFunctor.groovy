package com.github.mperry.fg.typeclass.concrete

import com.github.mperry.fg.typeclass.Functor
import fj.F
import fj.data.Option
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 10/04/2014.
 */
@TypeChecked
//@TypeChecked(TypeCheckingMode.SKIP)
class OptionFunctor implements Functor<Option> {

    @Override
    def <A, B> Option<B> map(F<A, B> f, Option<A> fa) {
        fa.map(f)
    }
}
