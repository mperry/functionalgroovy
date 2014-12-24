package com.github.mperry.fg.typeclass

import com.github.mperry.fg.typeclass.concrete.OptionMonad
import fj.Equal
import fj.F
import fj.data.Option
import groovy.transform.TypeChecked
import org.junit.Test

import static fj.data.Option.none
import static fj.data.Option.some

/**
 * Created by MarkPerry on 7/09/2014.
 */
@TypeChecked
class OptionMonadTest {

    OptionMonad monad() {
        new OptionMonad()
    }

    @Test
    void sequence() {
        assert(monad().sequence([some(3), some(2), some(5)]) == some([3, 2, 5]))
        assert(monad().sequence([some(3), none(), some(5)]) == none())
    }

    @Test
    void traverse() {
        def even = { Integer i -> i % 2 == 0 ? some(i) : none()} as F
        assert(monad().traverse([2, 4, 6], even) ==  some([2, 4, 6]))
        assert(monad().traverse([2, 3, 6], even) ==  none())
    }


}
