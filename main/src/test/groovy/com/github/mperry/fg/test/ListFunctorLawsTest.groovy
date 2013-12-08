package com.github.mperry.fg.test

import fj.F
import fj.F3
import fj.test.Arbitrary
import fj.test.Coarbitrary
import fj.test.Property
import groovy.transform.TypeChecked
import org.junit.Test

import static com.github.mperry.fg.test.PropertyTester.showAll
import static fj.Function.compose
import static fj.test.Arbitrary.*
import static fj.test.Arbitrary.arbF
import static fj.test.Arbitrary.arbList
import static fj.test.CheckResult.summary
import static fj.test.Coarbitrary.*
import static fj.test.Property.prop
import static fj.test.Property.property

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 6/12/13
 * Time: 3:31 PM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class ListFunctorLawsTest {

	@Test
	void identity() {
		showAll { List<Integer> list ->
			list == list.collect {
				it
			}
		}
	}

	@Test
	void composition2() {
		def p2 = property(arbF(coarbInteger, arbString), arbF(coarbLong, arbInteger), arbList(arbLong), {
			F<Integer, String> f, F<Long, Integer> g, fj.data.List<Long> x ->
				def y = x.toJavaList()
				def list1 = y.map(compose(f, g))
				def list2 = y.map(g).map(f)
				prop(list1 == list2)
			} as F3<F<Integer, String>, F<Long, Integer>, fj.data.List<Long>, Property>
		)
		p2.checkBooleanWithNullableSummary(true)
	}

}
