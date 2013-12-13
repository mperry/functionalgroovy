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
class IO3StaticExtension {

    static <B> IO3<Stream<B>> sequenceWhile(IO3 clazz, Stream<IO3<B>> stream, F<B, Boolean> f) {
        if (stream.empty) {
            IO3.unit(Stream.nil())
        } else {
            def h = stream.head()
            def t = stream.tail()._1()
            h.flatMap({ B b ->
                if (!f.f(b)) {
                    IO3.unit(Stream.nil())
                } else {
                    def rest = sequenceWhile(clazz, t, f)
                    IO3.unit([b]).append(rest)
                }
            } as F)
        }
    }

}
