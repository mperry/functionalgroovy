package com.github.mperry.fg.typeclass.concrete

import com.github.mperry.fg.typeclass.Monad
import fj.F
import fj.data.Option
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 10/04/2014.
 */
@TypeChecked
//@TypeChecked(TypeCheckingMode.SKIP)
class OptionMonad extends Monad<Option> {

    @Override
    def <A, B> Option<B> flatMap(Option<A> ma, F<A, Option<B>> f) {
        ma.flatMap(f)
    }

	@Override
    def <A> Option<A> unit(A a) {
        Option.some(a)
    }

}
