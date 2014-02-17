package com.github.mperry.fg

import fj.F
import fj.P
import fj.P2
import fj.data.Stream
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

import static com.github.mperry.fg.Comprehension.foreach
import static junit.framework.Assert.assertTrue

/**
 * Created by MarkPerry on 13/01/14.
 */
@TypeChecked
class StateTest {

    def random = new Random(0)
    def oracle = [true, true, false, true, true, false]
    def shortOracle = [true, true, false, true,]

    /**
     * Tests manually creating a sequence of random booleans using a monad comprehension
     */
    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void test1() {

        def s = StateM.lift({ Random r -> P.p(r, r.nextBoolean()) } as F)
        def s2 = foreach {
            a << s
            b << s
            c << s
            d << s
            yield { [a, b, c, d] }
        }
        def p = s2.run(random)
        assertTrue(p._2() == shortOracle)
    }

    void print(P2 p) {
        println "<${p._1()}, ${p._2()}>"
    }

    /**
     * Tests manually creating a sequence of random booleans using flatMap/map, instead of a monad comprehension
     */
    @Test
    void test2() {
        def st1 = StateM.lift({ Random r -> P.p(r, r.nextBoolean()) } as F)
        def st2 = st1.flatMap({ Boolean a ->
            st1.flatMap({ Boolean b ->
                st1.flatMap({ Boolean c ->
                    st1.map({ Boolean d -> [a, b, c, d]} as F)
                } as F)
            } as F)
        } as F)
        def p = st2.run(random)
        assertTrue(p._2() == shortOracle)
    }

    /**
     * Automate creating a sequence of random booleans where the last state is the one to run
     */
    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void test3() {
        def st1 = StateM.lift({ Random r ->
            def b = r.nextBoolean()
            P.p(r, P.p(b, [b]))
        } as F)
        def str1 = Stream.iterate({ StateM<Random, Boolean> st2 ->
            st2.flatMap({ P2<Boolean, List<Boolean>> p2 ->
                StateM.lift({ Random r ->
                    def b = r.nextBoolean()
                    P.p(r, P.p(b, p2._2() + b))
                } as F)
            } as F)
        } as F, st1)
        def p = str1.take(6).last().run(random)
//        print(p._1())
        assertTrue(p._2()._2() == oracle)
    }

    /**
     * Builds the sequence of booleans from the fold
     */
    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void test4() {
        def st1 = StateM.lift({ Random r -> P.p(r, r.nextBoolean()) } as F)
        def str1 = Stream.repeat(st1)

        def result = str1.take(6).foldLeft({ StateM<Random, List<Boolean>> acc ->
                { StateM<Random, Boolean> st3 ->
                    st3.flatMap { Boolean b ->
                        acc.map { List<Boolean> list ->
                            [b] + list
                        }
                    }
                } as F
        } as F, StateM.lift({ Random r -> P.p(r, [])} as F))
        def p = result.run(random)
//        println(p._1())
        assertTrue(p._2() == oracle)
    }

}
