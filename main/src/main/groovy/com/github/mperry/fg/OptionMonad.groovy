package com.github.mperry.fg

import fj.F
import fj.data.Option
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 30/12/13.
 */
//@TypeChecked
class OptionMonad extends Monad<Option> {

//    @Override
    def <A, B> Option<B> flatMapTyped(Option<A> ma, F<A, Option<B>> f) {
        ma.bind(f)
    }

    @Override
    def <A, B> Option flatMap(Option ma, F<A, Option> f) {
        flatMapTyped(ma, f)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    @Override
    def <B> Option<B> unit(B b) {
       Option.<B>some(b)
    }

    @Override
    def <A, B> Option<B> map(Option<A> ma, F<A, B> f) {
        (Option<B>) super.map(ma, f)
    }

}
