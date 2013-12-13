package com.github.mperry.fg

import fj.F
import fj.P1
import fj.data.Option
import groovy.transform.TypeChecked
import groovyx.gpars.GParsPool
import groovyx.gpars.GParsPoolUtil
import groovyx.gpars.dataflow.Promise
import groovyx.gpars.pa.GParsPoolUtilHelper
import groovyx.gpars.scheduler.FJPool

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future
import java.util.concurrent.FutureTask

//import static com.github.mperry.fg.SimpleIOStaticExtension.defaultStrategy

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 13/12/13
 * Time: 9:24 PM
 * To change this template use File | Settings | File Templates.
 */
//@TypeChecked
class SimpleIOExtension {


    public static <A> Promise<A> asyncGpars(SimpleIO<A> io) {
        def p = new FJPool()
        def c = { -> io.run() }
        def af = GParsPoolUtil.asyncFun(c, p)
        def r = af.call()
        r

    }


    public static <A> Future<A> asyncJava(SimpleIO<A> io) {
        asyncJava(io, SimpleIO.defaultService())

    }

    public static <A> Future<A> asyncJava(SimpleIO<A> io, ExecutorService s) {
//        def s = SimpleIO.defaultService()

        def c = { -> io.run() }
        def r = s.submit(c as Callable)
//        def r = GParsPoolUtilHelper.callAsync(c)
        r

    }


    public static <A> fj.control.parallel.Promise<A> asyncFj(SimpleIO<A> io) {
        def c = { ->
            io.run()
        }
        def r = fj.control.parallel.Promise.promise(SimpleIO.defaultStrategy(), c as P1)
//        def r = new FutureTask<A>(c as Callable)
//        def r = GParsPoolUtil.callAsync(c)
//        r.run()
        r

    }



}
