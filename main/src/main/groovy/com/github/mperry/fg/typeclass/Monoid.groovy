package com.github.mperry.fg.typeclass

import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 9/04/2014.
 */
@TypeChecked
interface Monoid<A> {

    /**
     * mempty  :: a
     */
    A mempty()

    /**
     * mappend :: a -> a -> a
     */
    A mappend(A a1, A a2)

}
