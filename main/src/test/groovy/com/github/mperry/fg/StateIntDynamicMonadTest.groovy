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

    def loader = lambda().newLoader()
    def simpleClass = Integer.class
    def partial = partialClass(simpleClass)

    TypeLambda lambda() {
        new TypeLambda()
    }

    Class<? extends State<Integer, ?>> partialClass(Class clazz) {
        lambda().partialState(loader, clazz)
    }

    Class<? extends Monad<? extends State>> monadClass() {
        lambda().stateMonad(loader, partial, simpleClass)
    }

    Monad monad() {
        monadClass().newInstance()
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <A> Arbitrary<? extends State> arbState(Arbitrary<A> aa) {
        arbState(aa, simpleClass)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <A> Arbitrary<? extends State> arbState(Arbitrary<A> aa, Class<?> c) {
        def s = "{${c.canonicalName} i -> P.p(a, i)}"
        def sh = new GroovyShell(loader)
        def f = sh.evaluate(s)
        arbitrary(aa.gen.map({ A a -> partial.newInstance(f as F)} as F))
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
