package com.github.mperry.fg

import fj.F
import fj.F2
import groovy.transform.TypeChecked

/**
 * Created by MarkPerry on 11/01/14.
 *
 * The a is the value in the monad and w is the log value (a monoid)
 */
@TypeChecked
class Writer<W, A> {

    A value
    W log
    F2<W, W, W> plus

    Writer(A a, W w, F2<W, W, W> f) {
        value = a
        log = w
        plus = f
    }

    static Writer<W, A> lift(A a, W w, F2<W, W, W> f) {
        new Writer(a, w, f)
    }

    def Writer<W, A> tell(W w) {
        Writer.lift(value, plus.f(log, w), plus)
    }

    def <B> Writer<W, B> map(F<A, B> f) {
        Writer.lift(f.f(value), log, plus)
    }

    def <B> Writer<W, B> map(Closure c) {
        map(c as F)
    }

    def Writer<W, A> flatMap(F<A, Writer<W, A>> f) {
        def writer = f.f(value)
        Writer.lift(writer.value, plus.f(log, writer.log), plus)
    }

    def Writer<W, A> flatMap(Closure c) {
        flatMap(c as F)
    }

    static Writer<W, A> lift(A a) {
        Writer.lift(a, STRING_EMPTY, STRING_CONCAT)
    }

    static Writer<W, A> log(A a) {
        Writer.lift(a, LOG_FUNCTION.f(a), STRING_CONCAT)
    }

    static F<A, Writer<W, A>> log() {
        { A a -> Writer.lift(a, LOG_FUNCTION.f(a), STRING_CONCAT) } as F
    }

    static F<Object, String> LOG_FUNCTION = { Object o -> "Added $o to the log\n"} as F
    static F2<String, String, String> STRING_CONCAT = {String a, String b -> a + b} as F2
    static String STRING_EMPTY = ""

}
