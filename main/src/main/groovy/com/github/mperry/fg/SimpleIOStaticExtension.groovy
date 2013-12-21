package com.github.mperry.fg

import fj.F
import fj.control.Trampoline
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

    static <A> SimpleIO<Stream<A>> sequenceWhile(SimpleIO clazz, Stream<SimpleIO<A>> stream, F<A, Boolean> f) {
        if (stream.empty) {
            SimpleIO.lift(Stream.nil())
        } else {
            stream.head().flatMap({ A a ->
                if (!f.f(a)) {
                    SimpleIO.lift(Stream.nil())
                } else {
                    def t = stream.tail()._1()
                    sequenceWhile(clazz, t, f).map({ Stream<A> s -> s.cons(a)} as F<Stream<A>, Stream<A>>)
                }
            } as F)
        }
    }

    static <A> Trampoline<SimpleIO<Stream<A>>> sequenceWhileC(SimpleIO clazz, Stream<SimpleIO<A>> stream, F<A, Boolean> f) {
        if (stream.empty) {
            Trampoline.pure(SimpleIO.lift(Stream.<A>nil()))
        } else {
            Trampoline.pure(stream.head()).bind({ SimpleIO<A> io ->
                // return type of method, Trampoline<SimpleIO<Stream<A>>>
                Trampoline.pure(
                    // SimpleIO<Stream<A>>
                    io.flatMap({ A a ->
                        // return SimpleIO<Stream<A>>
                        if (!f.f(a)) {
                            SimpleIO.lift(Stream.nil())
                        } else {
                            def t = stream.tail()._1()
                            sequenceWhileC(clazz, t, f).run().map({ Stream<A> s -> s.cons(a) } as F<Stream<A>, Stream<A>>)

                        }
                    } as F)
                )
            } as F)
        }
    }

}
