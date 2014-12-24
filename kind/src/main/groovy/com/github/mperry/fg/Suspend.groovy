package com.github.mperry.fg

import groovy.transform.Canonical
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 29/11/2014.
 */
@TypeChecked
@Canonical
class Suspend<K, A> extends Free<K, A> {

    K<A> value

    static Suspend unit(K<A> v) {
        new Suspend(v)
    }

}
