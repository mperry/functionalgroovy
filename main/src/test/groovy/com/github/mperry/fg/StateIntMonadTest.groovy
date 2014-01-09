package com.github.mperry.fg

import fj.F
import fj.Unit
import fj.test.Arbitrary
import fj.test.Coarbitrary
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

import static fj.test.Arbitrary.arbF
import static fj.test.Arbitrary.arbF
import static fj.test.Arbitrary.arbF
import static fj.test.Arbitrary.arbInteger
import static fj.test.Arbitrary.arbInteger
import static fj.test.Arbitrary.arbLong
import static fj.test.Arbitrary.arbString
import static fj.test.Arbitrary.arbitrary
import static fj.test.Coarbitrary.coarbInteger
import static fj.test.Coarbitrary.coarbInteger

/**
 * Created by MarkPerry on 9/01/14.
 */
class StateIntMonadTest {

    StateIntMonad<Unit> monad() {
        StateIntMonad.empty()
    }

}
