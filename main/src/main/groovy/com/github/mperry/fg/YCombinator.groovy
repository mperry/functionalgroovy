package com.github.mperry.fg

import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 23/01/14.
 */
//@TypeChecked
class YCombinator {

    static def c2(def f) {
        { x ->
            f(f)(x)
        }
    }

    static def Y(Closure fx) {
        def g = { Closure f ->
            fx(c2(f))
        }
        c2(g)
    }  //fx represents functional, f function

}
