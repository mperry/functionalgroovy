package com.github.mperry.fg

import fj.F
import fj.P2
import fj.data.Option
import fj.data.Stream
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 20/01/14.
 */
@TypeChecked
class StreamStaticExtension2 {

    @TypeChecked(TypeCheckingMode.SKIP)
    static <A, B> Stream<A> unfold(Stream s, B b, F<B, Option<P2<A, B>>> f) {
        Stream.unfold(f, b)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    static <A, B> Stream<A> unfold(Stream s, B b, Closure<Option<P2<A, B>>> f) {
        unfold(s, b, f as F)
    }

}
