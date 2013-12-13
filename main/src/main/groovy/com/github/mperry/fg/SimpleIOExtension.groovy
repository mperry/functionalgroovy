package com.github.mperry.fg

import fj.P1
import groovy.transform.TypeChecked
import groovyx.gpars.GParsPoolUtil
import groovyx.gpars.scheduler.FJPool

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

@TypeChecked
class SimpleIOExtension {

    public static <A> groovyx.gpars.dataflow.Promise<A> asyncGpars(SimpleIO<A> io) {
        def p = new FJPool()
        def c = { -> io.run() }
        def af = GParsPoolUtil.asyncFun(c, p)
        def r = af.call()
        (groovyx.gpars.dataflow.Promise<A>) r
    }

    public static <A> Future<A> asyncJava(SimpleIO<A> io) {
        asyncJava(io, SimpleIO.defaultService())
    }

    public static <A> Future<A> asyncJava(SimpleIO<A> io, ExecutorService s) {
        def c = { -> io.run() }
        def r = s.submit(c as Callable)
        r
    }

    public static <A> fj.control.parallel.Promise<A> asyncFj(SimpleIO<A> io) {
        def c = { ->
            io.run()
        }
        def r = fj.control.parallel.Promise.promise(SimpleIO.defaultStrategy(), c as P1)
        r
    }

}
