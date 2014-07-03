package com.github.mperry.fg

import fj.F
import fj.F1Functions
import fj.F2
import fj.P
import fj.P2
import fj.Unit
import fj.data.Either
import fj.data.Option
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * Created by MarkPerry on 14/01/14.
 */
@TypeChecked
class Lens<A, B> {

    F<A, B> get
    F2<A, B, A> set

    @TypeChecked(TypeCheckingMode.SKIP)
    static <C, D> Lens<C, D> lift(F<C, D> g, F2<C, D, C> s) {
        new Lens(g, s)
    }

    Lens(F<A, B> g, F2<A, B, A> s) {
        get = g
        set = s
    }

    B get(A a) {
        get.f(a)
    }

    A set(A a, B b) {
        set.f(a, b)
    }

    A mod(A a, F<B, B> f) {
        set(a, f.f(get(a)))
    }

    State<A, B> mod(F<B, B> f) {
        State.lift({ A a ->
            def b = f.f(get(a))
            P.p(set(a, b), b)
        } as F)
    }

    State<A, B> mod(Closure<B> c) {
        mod(c as F)
    }

    def <C> Lens<A, C> andThen(Lens<B, C> lens) {
        lens.compose(this)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <C> State<A, C> map(F<B, C> f) {
        // WARNING: this is implemented differently from
        // http://scalaz.github.io/scalaz/scalaz-2.9.1-6.0.4/doc.sxr/scalaz/Lens.scala.html
        // I have the tuple in the opposite order, i.e. P.p(a, f.f(get(a)))
        // I think this is ok as the trait States has the method "state"
        // def state[S, A](f: S => (S, A)): State[S, A] = new State[S, A] {
        // the order of the tuple returned by f is swapped compared to me
        State.lift({ A a -> P.p(a, f.f(get(a)))} as F)
    }

    def <C> State<A, C> flatMap(F<B, State<A, C>> f) {
        State.lift({ A a -> f.f(get(a)).run(a)} as F)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <C> Lens<C, B> compose(Lens<C, A> lens) {
        new Lens(
            F1Functions.o(get, lens.get),
            { C c, B b -> lens.set(c, set(lens.get(c), b)) } as F2
        )
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    def <C, D> Lens<P2<A, C>, P2<B, D>> product(Lens<C, D> lens) {
        new Lens(
                { P2<A, C> p -> P.p(get(p._1()), lens.get(p._2())) } as F,
                { P2<A, C> ac, P2<B, D> bd -> P.p(set(ac._1(), bd._1()), lens.set(ac._2(), bd._2())) } as F2
        )
    }

    def <C> Lens<Either<A, C>, B> sum(Lens<C, B> lens) {
        new Lens(
                { Either<A, C> e ->
                    e.isLeft() ? get(e.left().value()) : lens.get(e.right().value())
                } as F,
                { Either<A, C> e, B b ->
                    e.isLeft() ? Either.left(set(e.left().value(), b)) : lens.set(e.right().value(), b)
                } as F2
        )
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    State<A, B> update(B b) {
        State.lift({ A a ->
            P.p(set(a, b), b)
        } as F)
    }

    State<A, B> update(F<Unit, B> f) {
        update(f.f(Unit.unit()))
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    State<A, B> state() {
        State.lift({ A a -> P.p(a, get(a))} as F)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    static <K, V> Lens<Map<K, V>, Option<V>> mapLens(K k) {
        new Lens(
                { Map m -> Option.fromNull(m.get(k)) } as F,
                { Map m, Option<V> o ->
                    o.map { V v -> m + [(k): v]}.orSome(m - [k: m.get(k)])
                } as F2
        )
    }

    static <K> Lens<Set<K>, Boolean> setLens(K k) {
        new Lens(
                { Set<K> s -> s.contains(k) } as F,
                { Set<K> s, Boolean b ->
                    if (b) {
                        s.contains(k) ? s : ((Set<K>) s.clone()).add(k)
                    } else {
                        s.minus(k)
                    }
                } as F2
        )

    }

}
