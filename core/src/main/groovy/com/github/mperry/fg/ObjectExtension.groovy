package com.github.mperry.fg

import fj.F
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 15/01/14.
 */
@TypeChecked
class ObjectExtension {

    static Boolean isSubInstanceOf(Object o, Class c) {
        c.isAssignableFrom(o.class)
    }

    static Boolean isDirectInstanceOf(Object o, Class c) {
        o.class == c
    }

    static Boolean isProperSubInstanceOf(Object o, Class c) {
        isSubInstanceOf(o, c) && !isDirectInstanceOf(o, c)
    }

}
