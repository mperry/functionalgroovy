package com.github.mperry.fg

import com.github.mperry.fg.typeclass.Monad
import com.github.mperry.fg.typeclass.concrete.ListMonad
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.junit.Test

import static com.github.mperry.fg.test.ArbitraryCompanion.*
import static fj.test.Arbitrary.arbF
import static fj.test.Arbitrary.*
import static fj.test.Coarbitrary.*

/**
 * Created by MarkPerry on 10/01/14.
 */
@TypeChecked
class ListMonadTest {

    Monad monad() {
        new ListMonad<Integer>()
    }

    @Test
    @TypeChecked(TypeCheckingMode.SKIP)
    void leftIdentity() {
        def f = arbF(coarbInteger, arbJavaList(arbInteger))
        new MonadLaws().leftIdentity(monad(), f, arbInteger)
    }

    @Test
    void rightIdentity() {
        new MonadLaws().rightIdentity(monad(), arbJavaList(arbInteger))
    }

    @Test
    void associativity() {
        new MonadLaws().associativity(monad(), arbJavaList(arbInteger), arbFInvariant(arbJavaList(arbString)), arbFInvariant(arbJavaList(arbLong)))
    }

}
