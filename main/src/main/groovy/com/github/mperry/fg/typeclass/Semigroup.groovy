package com.github.mperry.fg.typeclass

import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 9/04/2014.
 */
@TypeChecked
interface Semigroup<A> {

    /**
     * (<>) :: a -> a -> a
     */
    A append(A a1, A a2)





}
