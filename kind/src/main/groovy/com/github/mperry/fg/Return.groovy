package com.github.mperry.fg

import groovy.transform.Canonical
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 29/11/2014.
 */
@TypeChecked
@Canonical
class Return<K, A> extends Free<K, A> {

    A a

    static Return<K, A> unit(A a) {
        new Return(a)
    }

}
