package com.github.mperry.fg.typeclass.trait

import fj.F
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 15/04/2014.
 */
// TODO, this does not compile when uncommented
//@TypeChecked
class ListMonadT
//        implements MonadT<List>
{

//    @Override
    def <A> List<A> unit(A a) {
        [a]
    }

//    @Override
    def <A, B> List<B> flatMap(List<A> ma, F<A, List<B>> f) {
        ma.flatMap(f)
    }
}
