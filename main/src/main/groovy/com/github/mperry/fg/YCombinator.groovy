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

    /**
     * Strict applicative order Y combinator
     *
     * (define Y
     *      (lambda (f)
     *          ((lambda (x) (x x))
     *          (lambda (x) (f (lambda (y) ((x x) y)))))))
     *
     * @param fx
     * @return
     */
    static def Y(Closure f) {
        def h = { x ->
            { y ->
                x(x)(y)
            }
        }
        def g = { Closure x ->
            f(h(x))
        }
        h(g)
    }  //fx represents functional, f function

}
