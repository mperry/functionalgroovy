package com.github.mperry.fg.typeclass.concrete

import com.github.mperry.fg.typeclass.Monad
import fj.F
import fj.data.IO
import fj.data.IOFunctions
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 15/07/2014.
 */
@TypeChecked
class IOMonad extends Monad<IO> {

    @Override
    def <A> IO<A> unit(A a) {
        { -> a } as IO
    }

    @Override
    def <A, B> IO<B> flatMap(IO<A> io, F<A, IO<B>> f) {
//        IOFunctions.bind(io, f)
        { -> f.f(io.run()).run() } as IO<B>
    }

}
