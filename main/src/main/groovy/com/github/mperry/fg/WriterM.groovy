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
class WriterM<W, A> {

    A value
    W log
    F2<W, W, W> plus

    WriterM(A a, W w, F2<W, W, W> f) {
        value = a
        log = w
        plus = f
    }

    static <W, B> WriterM<W, B> lift(B b, W w, F2<W, W, W> f) {
        new WriterM(b, w, f)
    }

    def WriterM<W, A> tell(W w) {
        WriterM.lift(value, plus.f(log, w), plus)
    }

    def <B> WriterM<W, B> map(F<A, B> f) {
        WriterM.lift(f.f(value), log, plus)
    }

    def <B> WriterM<W, B> flatMap(F<A, WriterM<W, B>> f) {
        def writer = f.f(value)
        WriterM.lift(writer.value, plus.f(log, writer.log), plus)
    }

    static <B> WriterM<String, B> lift(B b) {
        WriterM.lift(b, STRING_EMPTY, STRING_CONCAT)
    }

    static <A> WriterM<String, A> log(A a) {
        WriterM.lift(a, LOG_FUNCTION.f(a), STRING_CONCAT)
    }

    static <A, W> F<A, WriterM<String, A>> log() {
        { A a -> WriterM.lift(a, LOG_FUNCTION.f(a), STRING_CONCAT) } as F
    }

    static F<Object, String> LOG_FUNCTION = { Object o -> "Added $o to the log\n"} as F
    static F2<String, String, String> STRING_CONCAT = {String a, String b -> a + b} as F2
    static String STRING_EMPTY = ""

}
