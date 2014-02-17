package com.github.mperry.fg.state

import com.github.mperry.fg.StateM
import fj.F
import fj.P
import fj.P2
import fj.Unit
import groovy.transform.Canonical
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

import static com.github.mperry.fg.state.Input.*

/**
 * Created by MarkPerry on 17/02/14.
 */
@TypeChecked
@Canonical
class Machine {

    Boolean locked
    Integer items
    Integer coins


    @TypeChecked(TypeCheckingMode.SKIP)
    static StateM<Machine, Machine> simulate(List<Input> list) {
        def s2 = StateM.<Machine>get()
//        def s = StateM.<Machine, Machine>lift({ Machine m ->
//            P.p(m, m)
//        } as F)
        simulate(list, s2)

    }

    static StateM<Machine, Machine> simulate(List<Input> list, StateM<Machine, Machine> state) {
        if (list.empty) {
            state
        } else {
            def h = list.head()

            def s4 = state.map { Machine m ->
                next(m, h)
            }
            def s3 = new StateM<Machine, Machine>({ Machine m ->
                def m2 = next(m, h)
                P.p(m2, m2)
            } as F)
//            def s2 = StateM.<Machine, Machine>lift({ Machine m ->
//                def m2 = next(m, h)
//                P.p(m2, m2)
//            } as F)
            simulate(list.tail(), s4)
        }
    }

    static Machine next(Machine m, Input i) {
        if (m.items == 0) {
            m
        } else if (i == COIN && !m.locked) {
            m
        } else if (i == TURN && m.locked) {
            m
        } else if (i == COIN && m.locked) {
            new Machine(false, m.items, m.coins + 1)
        } else if (i == TURN && !m.locked) {
            new Machine(true, m.items - 1, m.coins)
        }
    }

    static void main(def args) {
        def m = new Machine(true, 5, 0)
        def inputs = [COIN, TURN, TURN, COIN, COIN, TURN]
        def s = simulate(inputs)
        def m2 = (Machine) s.eval(m)
        println m2
        println("<${m2.items}, ${m2.coins}>")
    }

//    @Test
//    void test1() {
//        def m = new Machine(false, 5, 0)
//        def inputs = [COIN, TURN, TURN, COIN, COIN, TURN]
//        def s = simulate(inputs)
//        def p = s.eval(m)
//        println("<${m.items}, ${m.coins}>")
//    }


}
