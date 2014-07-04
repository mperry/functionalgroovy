package com.github.mperry.fg.typeclass

import com.github.mperry.fg.typeclass.concrete.ListApplicative
import com.github.mperry.fg.typeclass.concrete.ListFunctor
import fj.F
import fj.F2Functions
import groovy.transform.TypeChecked
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * Created by mperry on 2/07/2014.
 */
@TypeChecked
class ListApplicativeTest {

    @Test
    void test1() {
        def listFunctor = new ListFunctor()
		def app = new ListApplicative()

		def list1 = (1..3).toList().map { { Integer a -> 4 + a } as F }
		def list2 = app.apply(list1, [1, 2, 3])
        def list3 = listFunctor.map([1, 2, 3], F2Functions.curry({ Integer a, Integer b -> a * b }))
        def list4 = app.apply(list3, [1, 2, 3])

        println list1
		println list2
		println list3
        println list4
    }

    @Test
    void testSeq() {
        def app = new ListApplicative()
        def list1 = app.sequenceA([[1, 2, 3], [4, 5, 6]])
        assertTrue(list1 == [[1,4],[1,5],[1,6],[2,4],[2,5],[2,6],[3,4],[3,5],[3,6]])
        println list1

		def list2 = app.sequenceA([[1,2,3],[4,5,6],[3,4,4],[]])
		println list2
		println([[1, 2, 3], [4, 5, 6]].combinations())
	}

}
