package com.github.mperry.fg

import fj.F
import fj.data.Stream
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

/**
 * Created by MarkPerry on 20/12/13.
 */
@TypeChecked
class StackOverflow {

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void testRecursive() {
        // stack overflow with about 10000 elements
        def stream = 1.to(1000).map({SimpleIO.lift("mark$it")} as F)
        def singleIo = SimpleIO.sequenceWhileR(stream, {String s -> true} as F)
        def result = singleIo.run().toList().toJavaList()
//        def show = result.toString()
//        println "result: $show"
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void test2() {
        // stackoverflow with ~10000 elements
        def smallStream = 1.to(1000).map({SimpleIO.lift("mark$it")} as F)
        def tramp = SimpleIO.sequenceWhileC(smallStream, {String s -> true} as F)
        def io2 = tramp.run()
        def result = io2.run()
//        println "result: ${result.toList()}"
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void testLoop() {
        def stream = 1.to(10000).map({SimpleIO.lift("mark$it")} as F)
        def singleIo = SimpleIO.sequenceWhile(stream, {String s -> true} as F)
        def result = singleIo.run().toList().toJavaList()
//        def show = result.toString()
//        println "result: $show"
    }

}
