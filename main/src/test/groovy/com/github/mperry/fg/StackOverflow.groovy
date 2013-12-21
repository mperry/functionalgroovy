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
    void test1() {
        def smallStream = 1.to(5).map({SimpleIO.lift("mark$it")} as F)
        def io = SimpleIO.lift("mark test")
        def infinite = Stream.repeat(io)
        def use = smallStream
//        def use = infinite
        def singleIo = SimpleIO.sequenceWhile(use, {String v -> v != "mark4"} as F)
        def result = singleIo.run()
        println "result: ${result.toList()}"
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void test2() {
        def smallStream = 1.to(5).map({SimpleIO.lift("mark$it")} as F)
        def io = SimpleIO.lift("mark test")
        def infinite = Stream.repeat(io)
//        def use = smallStream
        def use = infinite
//        def singleIo = SimpleIOStaticExtension.sequenceWhileC(SimpleIO.empty(), use, {String s -> s != "mark4"} as F)
        def singleIo = SimpleIO.sequenceWhileC(use.take(1000), {String s -> s != "mark4"} as F)
        def result = singleIo.run().run()
//                .run()
        println "result: ${result.toList()}"
    }


}
