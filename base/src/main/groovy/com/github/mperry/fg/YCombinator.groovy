package com.github.mperry.fg

import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 23/01/14.
 */
//@TypeChecked
class YCombinator {

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
        def h = { Closure x ->
            // y is the value passed in
            f { y ->
                x(x)(y)
            }
        }
        h(h)
    }

    static def Y2(def f) {
        f({ x -> Y2(f)(x)})

    }

}
