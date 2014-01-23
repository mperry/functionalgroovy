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
        def g = { Closure x ->
            x(x)
        }
        def h = { x ->
            f { y ->
                x(x)(y)
            }
        }
        g(h)
    }

}
