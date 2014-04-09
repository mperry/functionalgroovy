package com.github.mperry.fg

import com.github.mperry.fg.typeclass.Monad
import fj.F
import fj.test.Arbitrary
import fj.test.Coarbitrary
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
class StateDynamicMonadTest {

    @Test
    void allLaws() {
        test(newLoader(), Integer.class, arbInteger, coarbInteger, arbString, coarbString, arbLong)
    }

    GroovyClassLoader newLoader() {
        new GroovyClassLoader(this.class.classLoader)
    }

    def <A, B, C> void test(GroovyClassLoader loader, Class<?> stateClass, Arbitrary<A> arbAction,
                      Coarbitrary<A> coarbAction, Arbitrary<B> arbAction2, Coarbitrary<B> coarbAction2,
                      Arbitrary<C> arbAction3) {

        def ps = partialState(loader, stateClass)
        def m = monad(loader, stateClass, ps)
        def arbCompletedState = arbState(loader, arbAction, stateClass, ps)

        leftIdentity(m, arbCompletedState, arbAction, coarbAction)
        rightIdentity(m, arbCompletedState)
        associativity(loader, m, stateClass, ps, arbAction, coarbAction, arbAction2, coarbAction2, arbAction3)

    }

    def <A> void leftIdentity(Monad m, Arbitrary<? extends State> arbState,
                              Arbitrary<A> arbAction, Coarbitrary<A> coarbAction) {
        new MonadLaws().leftIdentity(m, arbF(coarbAction, arbState), arbAction)
    }

    void rightIdentity(Monad m, Arbitrary<? extends State> arbState) {
        new MonadLaws().rightIdentity(m, arbState)
    }

    def <A, B, C, D> void associativity(
            GroovyClassLoader loader, Monad m, Class<?> stateClass, Class<?> partialStateClass,
            Arbitrary<A> arbAction1, Coarbitrary<A> coarbAction1, Arbitrary<B> arbAction2, Coarbitrary<B> coarbAction2, Arbitrary<C> arbAction3) {

        def arbState1 = arbState(loader, arbAction1, stateClass, partialStateClass)
        def arbState2 = arbState(loader, arbAction2, stateClass, partialStateClass)
        def arbState3 = arbState(loader, arbAction3, stateClass, partialStateClass)

        new MonadLaws().associativity(m, arbState1, arbF(coarbAction1, arbState2),
                arbF(coarbAction2, arbState3))
    }


    Class<?> partialState(GroovyClassLoader loader, Class<?> stateClass) {
        def ps = new TypeLambda().partialStateApplication(loader, stateClass)
        ps
    }

    def <A> Monad monad(GroovyClassLoader loader, Class<?> stateClass, Class<?> partialStateClass) {
        def lambda = new TypeLambda()
        def monadClass = lambda.stateMonad(loader, partialStateClass, stateClass)
        def monad = monadClass.newInstance()
        monad
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <A> Arbitrary<? extends State> arbState(GroovyClassLoader loader, Arbitrary<A> arbAction, Class<?> stateClass,
                                                Class<?> partialStateClass) {
        def text = "{${stateClass.canonicalName} s -> P.p(a, s)}"
        def sh = new GroovyShell(loader)
        def f = sh.evaluate(text)
        arbitrary(arbAction.gen.map({ A a -> partialStateClass.newInstance(f as F)} as F))
    }

}
