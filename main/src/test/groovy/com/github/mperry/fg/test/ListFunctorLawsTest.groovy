package com.github.mperry.fg.test

import fj.F
import fj.F3
import fj.test.Arbitrary
import fj.test.Coarbitrary
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
	void test3() {
		def f3 = {
			String s, Integer i, Long l ->
				prop(true)
		} as F3
		def f4 = {
			F<Integer, String> f, F<Long, Integer> g, fj.data.List<Long> l ->
				true
				prop(true)
		} as F3

		def p = property(arbF(coarbInteger, arbString), arbF(coarbLong, arbInteger), arbList(arbLong), f4)
		def cr = p.check()
		p.checkBooleanWithNullableSummary(true)
	}

	@Test
	void composition() {

		def f3 = {
			F<Integer, String> f, F<Long, Integer> g, fj.data.List<Long> x ->
				prop(true)
		} as F3
		def p = property(arbF(coarbInteger, arbString), arbF(coarbLong, arbInteger), arbList(arbLong), f3)
		def p2 = property(arbF(coarbInteger, arbString), arbF(coarbLong, arbInteger), arbList(arbLong), {
				F<Integer, String> f, F<Long, Integer> g, fj.data.List<Long> x ->

					int z = 0
					def y = x.toJavaList()
					def c = compose(f, g).toClosure()
					def s1 = y.collect(compose(f, g).toClosure())
					def s2 = x.toJavaList().collect{Long it -> g.f(it)}.collect{Integer it -> f.f(it)}
					def s3 = x.toJavaList().collect(g.toClosure()).collect{f.toClosure()}
//					prop(s1 == s2)
					prop(s1 == s3)
//					prop(true)
			} as F3
		)

		def cr = p2.check()
		p2.checkBooleanWithNullableSummary(true)

	}

	@Test
	void composition2() {

		int zz = 0
		def f3 = {F<Integer, String> f, F<Long, Integer> g, fj.data.List<Long> x ->
			prop(true)
		} as F3
		def p1 = property(arbF(coarbInteger, arbString), arbF(coarbLong, arbInteger), arbList(arbLong), f3)
		def p2 = property(arbF(coarbInteger, arbString), arbF(coarbLong, arbInteger), arbList(arbLong), {
			F<Integer, String> f, F<Long, Integer> g, fj.data.List<Long> x ->
				int z = 0
				def y = x.toJavaList()
				def c = compose(f, g).toClosure()
				def s1 = y.collect(compose(f, g).toClosure())
				def s2 = y.collect{Long it -> g.f(it)}.collect{Integer it -> f.f(it)}
				def s3 = y.collect(g.toClosure()).collect(f.toClosure())
				def r1 = y.collect(g.toClosure())
				def r2 = r1.collect{f.toClosure()}
//					prop(s1 == s2)
				def b = s1 == s3
				prop(b)
//					prop(true)
			} as F3
		)

		def cr = p2.check()
		summary.println(cr);
//		p2.checkBooleanWithNullableSummary(true)

	}

	@Test
	void composition3() {
		int z = 0
	}



	@Test
	void simple() {

		def a = [1, 2, 3]
		def g = { Long l -> l.toInteger() } as F<Long, Integer>
		def f = { Integer i -> i.toString()} as F<Integer, String>
		def b = a.collect(g.toClosure())
		def c = b.collect(f.toClosure())
		println c
		int z = 0

	}


}
