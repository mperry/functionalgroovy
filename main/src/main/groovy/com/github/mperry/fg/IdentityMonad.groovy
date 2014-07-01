package com.github.mperry.fg

import com.github.mperry.fg.typeclass.Monad
import fj.F
import groovy.transform.TypeChecked

/**
 * Created by mperry on 1/07/2014.
 */
@TypeChecked
class IdentityMonad extends Monad<Identity> {

    @Override
    def <A, B> Identity<B> flatMap(Identity<A> id, F<A, Identity<B>> f) {
        f.f(id.item)
    }

    @Override
    def <B> Identity<B> unit(B b) {
        new Identity<B>(b)
    }

}
