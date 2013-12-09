package com.github.mperry.fg.test

import fj.F
import fj.F3
import groovy.transform.TypeChecked
import org.junit.Test

import static Specification.spec
import static com.github.mperry.fg.test.Specification.specAssert
import static fj.Function.compose
import static fj.test.Arbitrary.*
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
		specAssert { List<Integer> list ->
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
			} as F3
		)
		p2.checkBooleanWithNullableSummary(true)
	}

}
