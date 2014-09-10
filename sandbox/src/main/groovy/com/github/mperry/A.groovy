package com.github.mperry

import groovy.transform.Canonical
import groovy.transform.TypeChecked

/**
 * Created by mperry on 10/09/2014.
 */
@TypeChecked
@Canonical
class A {

    Object asType(Class clazz) {
        if (clazz == B) {
            new B()
        }

    }

}
