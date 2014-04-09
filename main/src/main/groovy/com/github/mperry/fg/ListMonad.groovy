package com.github.mperry.fg

import com.github.mperry.fg.typeclass.Monad
import fj.F
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 10/01/14.
 */
//@TypeChecked
class ListMonad extends Monad<List> {

    @TypeChecked(TypeCheckingMode.SKIP)
    def <B, C> List<C> flatMapTyped(List<B> mb, F<B, List<C>> f) {
        mb.flatMap(f)
    }

    @Override
    def <A, B> List flatMap(List ma, F<A, List> f) {
        flatMapTyped(ma, f)
    }

    @Override
//    @TypeChecked(TypeCheckingMode.SKIP)
    def <B> List<B> unit(B b) {
        [b]
    }
}
