package com.github.mperry.fg.typeclass.concrete

import com.github.mperry.fg.typeclass.Monad
import fj.F
import fj.data.IO
import fj.data.IOFunctions
import fj.data.SafeIO
import fj.data.Validation
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 7/09/2014.
 */
@TypeChecked
class SafeIOMonad extends Monad<SafeIO> {

    @Override
    def <A> SafeIO<A> unit(A a) {
        { -> a } as SafeIO
    }

    @Override
    def <A, B> SafeIO<B> flatMap(SafeIO<A> io, F<A, SafeIO<B>> f) {
        { -> IOFunctions.flatMap(io, { A a -> { -> f.f(a).run() } as IO<B> }) } as SafeIO<B>
    }

}
