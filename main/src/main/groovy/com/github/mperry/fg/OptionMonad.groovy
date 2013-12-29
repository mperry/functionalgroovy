package com.github.mperry.fg

import fj.F
import fj.data.Option
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 30/12/13.
 */
//@TypeChecked
class OptionMonad extends Monad<Option> {

    @Override
    def <A, B> Option<B> flatMap(Option<A> ma, F<A, Option<B>> f) {
        ma.bind(f)
    }

    @Override
    def <B> Option<B> unit(B b) {
       Option.<B>some(b)
    }
}
