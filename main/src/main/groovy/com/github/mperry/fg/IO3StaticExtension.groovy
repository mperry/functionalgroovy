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
class IO3StaticExtension {

    @TypeChecked
    static <B> IO3<Stream<B>> sequenceWhile2(IO3 clazz, Stream<IO3<B>> list, F<B, Boolean> f) {
        if (list.empty) {
            IO3.unit(Stream.<B>nil())
        } else {
            def h = list.head()
            def t = list.tail()
            h.flatMap({ B b ->
                if (!f.f(b)) {
                    IO3.unit(Stream.nil())
                } else {
                    def rest = sequenceWhile2(clazz, t._1(), f)
                    IO3.unit([b]).append(rest)
                }
            } as F)
        }
    }

}