package com.github.mperry.fg

import fj.F
import fj.Unit
import fj.test.Arbitrary
import fj.test.Coarbitrary
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

import static fj.test.Arbitrary.*
import static fj.test.Coarbitrary.*

/**
 * Created by MarkPerry on 9/01/14.
 */
@TypeChecked
class IdentityTest {

    IdentityM<Unit> monad() {
        IdentityM.idUnit()
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <A> Arbitrary<IdentityM<A>> arbId(Arbitrary<A> aa) {
        arbitrary(aa.gen.map({ A a -> IdentityM.lift(a) } as F))
    }

    Arbitrary<IdentityM<Integer>> arbIdInteger() {
        arbId(arbInteger)
    }

    @Test
//    @TypeChecked(TypeCheckingMode.SKIP)
    void leftIdentity() {
        def f = arbF(coarbInteger, arbIdInteger())
        new MonadLaws().leftIdentity(monad(), f, arbInteger)
    }

    @Test
    void rightIdentity() {
        new MonadLaws().rightIdentity(monad(), arbIdInteger())
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void associativity() {
        new MonadLaws().associativity(monad(), arbIdInteger(), arbF(coarbInteger, arbId(arbString)), arbF(Coarbitrary.coarbString, arbId(arbLong)))
    }

}
