package com.github.mperry.fg

import fj.F
import fj.data.Stream
import groovy.transform.TypeChecked

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 13/12/13
 * Time: 12:10 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class SimpleIOStaticExtension {

    static <B> SimpleIO<Stream<B>> sequenceWhile(SimpleIO clazz, Stream<SimpleIO<B>> stream, F<B, Boolean> f) {
        if (stream.empty) {
            SimpleIO.lift(Stream.nil())
        } else {
            stream.head().flatMap({ B b ->
                if (!f.f(b)) {
                    SimpleIO.lift(Stream.nil())
                } else {
                    def t = stream.tail()._1()
                    sequenceWhile(clazz, t, f).map({ Stream<B> s -> s.cons(b)} as F<Stream<B>, Stream<B>>)
                }
            } as F)
        }
    }

}
