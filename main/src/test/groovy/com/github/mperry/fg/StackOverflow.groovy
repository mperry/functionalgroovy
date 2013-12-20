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
        def singleIo = SimpleIO.sequenceWhile(smallStream, {String v -> v != "mark4"} as F)
        def result = singleIo.run()
        println "result: ${result.toList()}"
    }

}
