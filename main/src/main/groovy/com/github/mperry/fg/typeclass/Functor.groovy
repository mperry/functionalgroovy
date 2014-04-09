package com.github.mperry.fg.typeclass

import fj.F
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 9/04/2014.
 */
@TypeChecked
abstract class Functor<T> {

    abstract <A, B> T<B> fmap(T<A> fa, F<A, B> f);




}
