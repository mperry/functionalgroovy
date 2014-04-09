package com.github.mperry.fg.typeclass

import fj.F

/**
 * Created by MarkPerry on 9/04/2014.
 */
abstract class Applicative<T> {

    abstract <A> T<A> pure(A a)
    abstract <A, B> T<B> apply(T<F<A, B>> t1, T<A> t2)

}
