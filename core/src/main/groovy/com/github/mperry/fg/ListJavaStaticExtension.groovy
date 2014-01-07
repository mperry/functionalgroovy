package com.github.mperry.fg

import fj.data.Option
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 30/12/13.
 */
//@TypeChecked
class ListJavaStaticExtension {

    public static <A> java.util.List<A> repeat(List list, Integer n, A a) {
        def result = []
        for (int i = 0; i < n; i++) {
            result.add(a)
        }
        result
    }

}