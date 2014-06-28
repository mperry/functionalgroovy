package com.github.mperry.fg.typeclass.concrete

import com.github.mperry.fg.typeclass.Functor
import fj.F
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 10/04/2014.
 */
@TypeChecked
//@TypeChecked(TypeCheckingMode.SKIP)
class ListFunctor<A> implements Functor<List> {

    @Override
    def <B> List<B> fmap(F<A, B> f, List<A> list) {
        list.map(f)
    }

}
