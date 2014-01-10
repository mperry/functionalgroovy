package com.github.mperry.fg

import fj.F
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 10/01/14.
 */
@TypeChecked
class ListMonad extends Monad<List> {

    @Override
    @TypeChecked(TypeCheckingMode.SKIP)
    def <B, C> List<C> flatMap(List<B> mb, F<B, List<C>> f) {
        mb.flatMap(f)
    }

    @Override
    @TypeChecked(TypeCheckingMode.SKIP)
    def <B> List<B> unit(B b) {
        [b]
    }
}
