package com.github.mperry.fg

import fj.F
import fj.P1
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
            Trampoline.suspend({ ->
                Trampoline.pure(stream.head()).map({ SimpleIO<A> io ->
                    // return SimpleIO<Stream<A>>
                    io.flatMap({ A a ->
                        def b = f.f(a)
                        if (!b) {
                            SimpleIO.lift(Stream.<A>nil())
                        } else {
                            def tail = stream.tail()._1()
                            def tramp = sequenceWhileC(clazz, tail, f)
                            def io2 = tramp.run()
                            def f2 = ({ Stream<A> s -> s.cons(a)} as F<Stream<A>, Stream<A>>)
                            io2.map(f2)
                        }
                    } as F)
                } as F)
            } as P1)
        }
    }

}
