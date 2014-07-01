package com.github.mperry.fg.typeclass.concrete

import com.github.mperry.fg.typeclass.Applicative
import fj.F
import fj.P2
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 10/04/2014.
 */
@TypeChecked
//@TypeChecked(TypeCheckingMode.SKIP)
class ListApplicative extends Applicative<List> {

    @Override
    def <A> List<A> pure(A a) {
        [a]
    }

    @Override
    def <A, B> List<B> apply(List<F<A, B>> fs, List<A> list) {
        list.zip(fs).collect { P2<A, F<A, B>> p ->
            p._2().f(p._1())
        }
    }

    @Override
    def <A, B> List<B> fmap(F<A, B> f, List<A> list) {
        list.collect(f)
    }

}
