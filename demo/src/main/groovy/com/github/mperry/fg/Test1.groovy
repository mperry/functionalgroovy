package com.github.mperry.fg
/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 13/12/13
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
class Test1 {

    void fjAsync() {

        def console = IOConstants.consoleReadLine()
//        def io = console.promise()
//        def io = console.future()
        def io = console.fjPromise()
        println "Run future async"
        def f = io.run()
        println "Getting future"
//        def v = f.get()
        def v = f.claim()
        int z = 0
        println "Future value was $v"

    }

    void gparsAsync() {

        def console = IOConstants.consoleReadLine()
        def io = console.gparsPromise()
        println "Run future async"
        def f = io.run()
        println "Getting future"
        def v = f.get()
//        def v = f.claim()
        int z = 0
        println "Future value was $v"

    }

    void javaAsync() {

        def s = SimpleIO.defaultService()
        def console = IOConstants.consoleReadLine()
        def io = console.future(s)
        println "Run future async"
        def f = io.run()
        println "Getting future"
        def v = f.get()
//        def v = f.claim()
        int z = 0
        println "Future value was $v"
        s.shutdown()

    }



    static void main(def args) {
        def t = new Test1()
//        t.fjAsync()
//        t.gparsAsync()
        t.javaAsync()
    }
}
