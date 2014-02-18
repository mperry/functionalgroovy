package com.github.mperry.fg.state

import com.github.mperry.fg.State
import groovy.transform.Canonical
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import static com.github.mperry.fg.state.Input.COIN
import static com.github.mperry.fg.state.Input.TURN

/**
 * Created by MarkPerry on 18/02/14.
 */
@TypeChecked
@Canonical
class MachineSimulation {

    @TypeChecked(TypeCheckingMode.SKIP)
    State<Machine, Machine> simulate(List<Input> list) {
        def s2 = State.<Machine>get()
        simulate(list, s2)
    }

    State<Machine, Machine> simulate(List<Input> list, State<Machine, Machine> state) {
        if (list.empty) {
            state
        } else {
            def h = list.head()
            def s4 = state.map { Machine m ->
                next(m, h)
            }
            simulate(list.tail(), s4)
        }
    }

    Machine next(Machine m, Input i) {
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

}
