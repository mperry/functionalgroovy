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
        def console = IOConstants.stdinReadLine()
        def io = console.fjPromise()
        println "Enter FunctionalJava promise value"
        def p = io.run()
        def v = p.claim()
        println "Future value was $v"
    }

    void gparsAsync() {
        def console = IOConstants.stdinReadLine()
        def io = console.gparsPromise()
        println "Enter GPars promise value"
        def p = io.run()
        def v = p.get()
        println "Future value was $v"
    }

    void javaAsync() {
        def s = SimpleIO.defaultService()
        def console = IOConstants.stdinReadLine()
        def io = console.future(s)
        println "Enter Java future value"
        def f = io.run()
        def v = f.get()
        println "Future value was $v"
        s.shutdown()
    }

    static void main(def args) {
        def t = new Test1()
        t.fjAsync()
        t.gparsAsync()
        t.javaAsync()
    }
}
