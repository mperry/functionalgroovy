package com.github.mperry.fg

import fj.P1
import fj.control.parallel.Promise
import fj.control.parallel.Strategy
import groovy.transform.TypeChecked

import java.util.concurrent.Future

import static fj.control.parallel.Promise.promise
import static fj.control.parallel.Strategy.simpleThreadStrategy

/**
 * Created by MarkPerry on 18/04/2014.
 */
@TypeChecked
class FutureExtension {

    static <A> P1<A> p(Future<A> f ) {
        Strategy.obtain(f)
    }

    static <A> Promise<A> promise(Future<A> f) {
        FutureExtension.promise(f, simpleThreadStrategy())
    }

    static <A> Promise<A> promise(Future<A> f, Strategy s) {
        Promise.promise(s, p(f))
    }

}
