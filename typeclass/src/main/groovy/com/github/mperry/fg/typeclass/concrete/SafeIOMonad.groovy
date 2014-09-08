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
    def <A, E extends IOException> SafeIO<E, A> unit(A a) {
        { -> Validation.success(a) } as SafeIO
    }

    @Override
    def <A, B, E extends IOException> SafeIO<E, B> flatMap(SafeIO<E, A> io, F<A, SafeIO<E, B>> f) {
        { -> io.run().bind({ A a -> f.f(a).run()}) } as SafeIO<B>
    }

}
