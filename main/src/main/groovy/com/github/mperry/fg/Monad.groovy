package com.github.mperry.fg

import fj.F
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 30/12/13.
 */
@TypeChecked
abstract class Monad<M, A> {

    abstract <B> M<B> flatMap(M<A> ma, F<A, M<B>> f)

    abstract <B> M<B> unit(B b)

    def <B> M<B> map(M<A> ma, F<A, B> f) {
        flatMap(ma, { A a -> unit(f.f(a)) } as F)
    }



}
