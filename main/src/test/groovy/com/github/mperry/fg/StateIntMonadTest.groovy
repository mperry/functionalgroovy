package com.github.mperry.fg

import fj.F
import fj.P
import fj.Unit
import fj.test.Arbitrary
import fj.test.Coarbitrary
import fj.test.Gen
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
import static fj.test.Coarbitrary.*

/**
 * Created by MarkPerry on 9/01/14.
 */
@TypeChecked
class StateIntMonadTest {

    StateIntMonad monad() {
        new StateIntMonad()
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <A> Arbitrary<StateInt<A>> arbState(Arbitrary<A> aa) {
        arbitrary(aa.gen.map({ A a -> new StateInt({ Integer i -> P.p(a, i) } as F)} as F))
    }

    Arbitrary<StateInt<Integer>> arbStateInt() {
        arbState(arbInteger)
    }

    @Test
    void leftIdentity() {
        new MonadLaws().leftIdentity(monad(), arbF(coarbInteger, arbStateInt()), arbInteger)
    }

    @Test
    void rightIdentity() {
        new MonadLaws().rightIdentity(monad(), arbStateInt())
    }

    @Test
    void associativity() {
        new MonadLaws().associativity(monad(), arbStateInt(), arbF(coarbInteger, arbState(arbString)),
            arbF(coarbString, arbState(arbLong)))
    }

}
