package com.github.mperry.fg

import fj.F
import fj.F2
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

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

    static <W2, A2> WriterM<W2, A2> lift(A2 a, W2 w, F2<W2, W2, W2> f) {
        new WriterM(a, w, f)
    }

    // workaround for Groovy 2.3.0-rc-1, should type check
    @TypeChecked(TypeCheckingMode.SKIP)
    def WriterM<W, A> tell(W w) {
        WriterM.lift(value, plus.f(log, w), plus)
    }

    // workaround for Groovy 2.3.0-rc-1, should type check
    @TypeChecked(TypeCheckingMode.SKIP)
    def <B> WriterM<W, B> map(F<A, B> f) {
        WriterM.lift(f.f(value), log, plus)
    }

    def <B> WriterM<W, B> map(Closure c) {
        map(c as F)
    }

    // workaround for Groovy 2.3.0-rc-1, should type check
    @TypeChecked(TypeCheckingMode.SKIP)
    def WriterM<W, A> flatMap(F<A, WriterM<W, A>> f) {
        def writer = f.f(value)
        WriterM.lift(writer.value, plus.f(log, writer.log), plus)
    }

    def WriterM<W, A> flatMap(Closure c) {
        flatMap(c as F)
    }

    static <W2, A2> WriterM<W2, A2> lift(A2 a) {
        WriterM.lift(a, STRING_EMPTY, STRING_CONCAT)
    }

    static <W2, A2> WriterM<W2, A2> log(A2 a) {
        WriterM.lift(a, LOG_FUNCTION.f(a), STRING_CONCAT)
    }

    static <W2, A2> F<A2, WriterM<W2, A2>> log() {
        { A2 a -> WriterM.lift(a, LOG_FUNCTION.f(a), STRING_CONCAT) } as F
    }

    static F<Object, String> LOG_FUNCTION = { Object o -> "Added $o to the log\n"} as F
    static F2<String, String, String> STRING_CONCAT = {String a, String b -> a + b} as F2
    static String STRING_EMPTY = ""

}
