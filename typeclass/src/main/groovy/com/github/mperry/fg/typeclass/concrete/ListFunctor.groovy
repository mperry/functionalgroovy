package com.github.mperry.fg.typeclass.concrete

import com.github.mperry.fg.typeclass.Functor
import fj.F

/**
 * Created by MarkPerry on 10/04/2014.
 */
class ListFunctor implements Functor<List> {

    @Override
    def <A, B> List<B> fmap(F<A, B> f, List<A> list) {
        list.map(f)
    }

}
