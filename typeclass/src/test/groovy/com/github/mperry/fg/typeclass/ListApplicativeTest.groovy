package com.github.mperry.fg.typeclass

import com.github.mperry.fg.typeclass.concrete.ListApplicative
import com.github.mperry.fg.typeclass.concrete.ListFunctor
import com.github.mperry.fg.typeclass.concrete.OptionApplicative
import fj.F
import fj.F2Functions
import groovy.transform.TypeChecked
import org.junit.Assert
import org.junit.Test

import static fj.Function.curry
import static fj.data.Option.none
import static fj.data.Option.some
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertTrue

/**
 * Created by mperry on 2/07/2014.
 */
@TypeChecked
class ListApplicativeTest {


    @Test
    void test1() {
        def listFunctor = new ListFunctor()
        def list1 = listFunctor.map([1, 2, 3], F2Functions.curry({ Integer a, Integer b -> a * b }))


        def app = new ListApplicative()
        def list2 = app.apply(list1, [1, 2, 3])

        println list1
        println list2


    }

    @Test
    void testSeq() {
        def app = new ListApplicative()
        def s1 = app.sequenceA([[1, 2, 3], [4, 5, 6]])
        assertTrue(s1 == [[1,4],[1,5],[1,6],[2,4],[2,5],[2,6],[3,4],[3,5],[3,6]])
        println s1

    }

}
