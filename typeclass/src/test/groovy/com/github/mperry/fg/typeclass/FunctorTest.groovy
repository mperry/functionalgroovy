package com.github.mperry.fg.typeclass

import com.github.mperry.fg.typeclass.concrete.ListFunctor
import com.github.mperry.fg.typeclass.concrete.OptionFunctor
import fj.F
import fj.F1Functions
import fj.F2Functions
import fj.function.Integers
import groovy.transform.TypeChecked
import org.junit.Test

import static fj.F2Functions.curry
import static fj.data.Option.some

/**
 * Created by MarkPerry on 10/04/2014.
 */
@TypeChecked
class FunctorTest {

    @Test
    void test1() {
        def optionFunctor = new OptionFunctor()
        def listFunctor = new ListFunctor()
        def m = Integers.multiply
        def list1 = listFunctor.map([1, 2, 3], curry({ Integer a, Integer b -> a * b }))
        def list2 = listFunctor.map(list1, { F<Integer, Integer> f -> f.f(3) })
        println list1
        println list2

    }

}
