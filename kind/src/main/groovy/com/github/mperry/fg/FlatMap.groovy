package com.github.mperry.fg

import fj.F
import groovy.transform.Canonical
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 29/11/2014.
 */
@TypeChecked
@Canonical
class FlatMap<K, A, B> extends Free<K, B> {

    Free<K, A> sub // subcomputation
    F<A, Free<K, B>> k // continue


}
