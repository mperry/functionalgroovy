package com.github.mperry.fg

import com.github.mperry.fg.typeclass.*
import fj.F

/**
 * Created by MarkPerry on 29/11/2014.
 */
class FreeP1Monad extends Monad<FreeP1> {

    @Override
    def <A, B> FreeP1<B> flatMap(FreeP1<A> ma, F<A, FreeP1<B>> f) {
        ma.flatMap(f)
    }

    @Override
    def <B> FreeP1<B> unit(B b) {
        new Return(b)
    }

}
