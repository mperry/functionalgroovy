package com.github.mperry.fg.typeclass.concrete

import com.github.mperry.fg.typeclass.Applicative
import fj.F
import fj.data.Option

/**
 * Created by MarkPerry on 10/04/2014.
 */
class OptionApplicative<A> extends Applicative<Option<A>> {

    @Override
    def <A> Option<A> pure(A a) {
        Option.some(a)
    }

    @Override
    def <A, B> Option<B> apply(Option<F<A, B>> optF, Option<A> o) {
        o.flatMap { A a ->
            optF.map { F<A, B> f ->
                f.f(a)
            }
        }
    }

    @Override
    def <A, B> Option<B> fmap(F<A, B> f, Option<A> fa) {
        fa.map(f)
    }
}
