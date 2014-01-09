package com.github.mperry.fg

import fj.F
import fj.P
import fj.test.Arbitrary
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
import static fj.test.Coarbitrary.coarbString
import static junit.framework.Assert.assertTrue

/**
 * Created by MarkPerry on 10/01/14.
 */
//@TypeChecked
class StateIntDynamicMonadTest {

    def loader = new TypeLambda().newLoader()
    def partial = partialClass()

    @Test
    void noop() {
//        partialClass()
        monadClass()
        assertTrue(true)
    }

    TypeLambda lambda() {
        new TypeLambda()
    }

    Class<? extends State<Integer, ?>> partialClass() {
        lambda().partialState(loader, Integer.class)
    }

    Class<? extends Monad<?>> monadClass() {
        def c = partial
        def c2 = lambda().stateMonad(loader, c, Integer.class)
        c2
    }

    Monad monad() {
        monadClass().newInstance()
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <A> Arbitrary<? extends State> arbState(Arbitrary<A> aa) {
        arbitrary(aa.gen.map({ A a -> partial.newInstance({ Integer i -> P.p(a, i) } as F)} as F))
    }

    Arbitrary<? extends State> arbStateInt() {
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
