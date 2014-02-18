package com.github.mperry.fg.state

import groovy.transform.TypeChecked
import org.junit.Assert
import org.junit.Test

import static com.github.mperry.fg.state.Input.COIN
import static com.github.mperry.fg.state.Input.TURN
import static org.junit.Assert.assertTrue

/**
 * Created by MarkPerry on 18/02/14.
 */
@TypeChecked
class MachineTest {

    @Test
    void test1() {
        def m = new Machine(true, 5, 0)
        def sim = new MachineSimulation()
        def inputs = [COIN, TURN, TURN, COIN, COIN, TURN]
        def s = sim.simulate(inputs)
        def m2 = (Machine) s.eval(m)
//        println m2
        assertTrue(m2 == new Machine(true, 3, 2))
    }

}
