package com.github.mperry.fg

import fj.F
import fj.P
import fj.P2
import fj.P3
import fj.Unit
import groovy.transform.TypeChecked
import org.junit.Assert
import org.junit.Test

import static fj.P.*


/**
 * Created by MarkPerry on 14/02/14.
 */
@TypeChecked
class StateGame {

    String increment = "i"
    String decrement = "d"
    String toggle = "t"

    @Test
    void test1() {
        def s = playGame("tiitd")
        def i = s.eval(P.p(true, 0))
        println i
        def r = run([P.p("tiitd", true, 0), P.p("tiitd", false, 0)])
        def expected = [-1, 2]

        println r
        Assert.assertTrue(r == expected)

    }

    List<Integer> run(List<P3<String, Boolean, Integer>> list) {
        list.map { P3<String, Boolean, Integer> p ->
            def s = playGame(p._1())
            def i = s.eval(P.p(p._2(), p._3()))
            i
        }
    }

    StateM<P2<Boolean, Integer>, Unit> command(String s, P2<Boolean, Integer> p) {
        def h = s
        def on = p._1()
        def score = p._2()
        if (on && h == increment) {
            StateM.put(P.p(on, score + 1))
        } else if (on && h == decrement) {
            StateM.put(P.p(on, score - 1))
        } else if (h == toggle) {
            StateM.put(P.p(!on, score))
        } else {
            StateM.put(P.p(on, score))

        }

    }

    StateM<P2<Boolean, Integer>, Integer> playGame(String s) {
        if (s.length() == 0) {
            StateM.<P2<Boolean, Integer>>get().map { P2<Boolean, Integer> p ->
                p._2()
            }
        } else {
            StateM.<P2<Boolean, Integer>>get().flatMap { P2<Boolean, Integer> p ->
                def h = s[0]

                def on = p._1()
                def score = p._2()
                def result = command(h, p)
//                if (on && h == increment) {
//                    result = StateM.put(P.p(on, score + 1))
//                } else if (on && h == decrement) {
//                    result = StateM.put(P.p(on, score - 1))
//                } else if (h == toggle) {
//                    result = StateM.put(P.p(!on, score))
//                } else {
//                    result = StateM.put(P.p(on, score))
//
//                }
                result.flatMap({ Unit u ->
                    def tail = s.substring(1)
                    playGame(tail)

                } as F)


            }

        }


    }

}
