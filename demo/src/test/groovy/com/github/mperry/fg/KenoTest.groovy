package com.github.mperry.fg

import fj.P1
import fj.data.Stream
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

/**
 * Created by MarkPerry on 13/01/14.
 */
class KenoTest {

    def random = new Random(0)

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void test5() {
        def max = 3
        def choose = 1
        def s2 = Stream.repeat(random)
        def s3 = s2.map { Random r -> r.nextInt(max) + 1 }

        println s3.take(max).toList()
        def s4 = removeDuplicates(s3, max)
        println s4.take(max).toList()
        println s4.take(choose).toList()
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    Stream<Integer> removeDuplicates(Stream<Integer> s, Integer max) {
        if (s.empty || max == 0) {
            s
        } else {
            def h = s.head()
            Stream.cons(h, { ->
                def s2 = (max == 1) ? (Stream<Integer>) Stream.nil() : s.tail()._1()
                def rest = s2.filter { Integer i -> i != h }
                removeDuplicates(rest, max - 1)
            } as P1)
        }

    }

}
