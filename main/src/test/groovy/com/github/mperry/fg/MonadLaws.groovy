package com.github.mperry.fg

import fj.F
import fj.F2
import fj.F3
import fj.test.Arbitrary
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import static fj.test.Arbitrary.arbF
import static fj.test.Arbitrary.arbInteger
import static fj.test.Arbitrary.arbOption
import static fj.test.Arbitrary.arbString
import static fj.test.Coarbitrary.coarbInteger
import static fj.test.Property.prop
import static fj.test.Property.property
import static fj.test.Property.property

/**
 * Created by MarkPerry on 9/01/14.
 */
@TypeChecked
class MonadLaws {

    // Left identity: return a >>= f == f a
    @TypeChecked(TypeCheckingMode.SKIP)
    def <M, A, B> void leftIdentity(Monad m, Arbitrary<F<A, M<B>>> af, Arbitrary<A> aa) {
        def p = property(af, aa, {
            F<A, M<B>> f, A a ->
                def b = m.flatMap(m.unit(a), f).equals(f.f(a))
                prop(b)
        } as F2)
        p.checkOkWithSummary()
    }

    // Right identity: m >>= return == m
    @TypeChecked(TypeCheckingMode.SKIP)
    def <M, A> void rightIdentity(Monad monad, Arbitrary<M<A>> arb) {
        def p = property(arb, {
            M<A> m ->
                prop(monad.flatMap(m, monad.unit()).equals(m))
//                def u = monad.unit()
//                def m1 = monad.flatMap(m, u)
//                def b = m1.equals(m)
//                prop(b)
        } as F)
        p.checkOkWithSummary()
    }

    // Associativity: (m >>= f) >>= g == m >>= (\x -> f x >>= g)
    @TypeChecked(TypeCheckingMode.SKIP)
    def <M, A, B, C> void associativity(Monad m, Arbitrary<M<A>> ama, Arbitrary<F<A, M<B>>> af1, Arbitrary<F<B, M<C>>> af2) {
        def p = property(ama, af1, af2, {
            M<A> o, F<A, M<B>> f, F<B, M<C>> g ->
                prop(m.flatMap(m.flatMap(o, f), g).equals(m.flatMap(o, { A i -> m.flatMap(f.f(i), g) } as F)))
        } as F3)
        p.checkOkWithSummary()
    }

}
