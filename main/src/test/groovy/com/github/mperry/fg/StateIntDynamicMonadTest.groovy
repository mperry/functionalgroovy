package com.github.mperry.fg

import fj.F
import fj.test.Arbitrary
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

import static fj.test.Arbitrary.arbF
import static fj.test.Arbitrary.arbInteger
import static fj.test.Arbitrary.arbLong
import static fj.test.Arbitrary.arbString
import static fj.test.Arbitrary.arbitrary
import static fj.test.Coarbitrary.coarbInteger
import static fj.test.Coarbitrary.coarbString

/**
 * Created by MarkPerry on 10/01/14.
 */
//@TypeChecked
class StateIntDynamicMonadTest {

    def loader = lambda().newLoader()
    def stateClass = Integer.class
    def partialState = partialState(stateClass)

    TypeLambda lambda() {
        new TypeLambda()
    }

    Class<? extends State> partialState(Class clazz) {
        lambda().partialStateApplication(loader, clazz)
    }

    Class<? extends Monad<? extends State>> monadClass() {
        lambda().stateMonad(loader, partialState, stateClass)
    }

    Monad monad() {
        monadClass().newInstance()
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <A> Arbitrary<? extends State> arbState(Arbitrary<A> aa) {
        arbState(aa, stateClass)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <A> Arbitrary<? extends State> arbState(Arbitrary<A> aa, Class<?> stateType) {
        def text = "{${stateType.canonicalName} s -> P.p(a, s)}"
        def sh = new GroovyShell(loader)
        def f = sh.evaluate(text)
        arbitrary(aa.gen.map({ A a -> partialState.newInstance(f as F)} as F))
    }

    Arbitrary<? extends State> arbStateInt() {
        arbState(arbInteger)
    }

    @Test
    void allLaws() {
        new StateDynamicMonadTest().test(loader, stateClass, arbInteger, coarbInteger, arbString, coarbString, arbLong)
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
