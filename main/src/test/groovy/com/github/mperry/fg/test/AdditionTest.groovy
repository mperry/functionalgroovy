package com.github.mperry.fg.test

import fj.F2
import fj.Show
import fj.test.Arbitrary
import fj.test.Bool
import fj.test.Gen
import fj.test.Property
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 9/08/13
 * Time: 1:07 AM
 * To change this template use File | Settings | File Templates.
 */
class AdditionTest {

	@Test
	void commute() {
		def p = Property.property(Arbitrary.arbInteger, Arbitrary.arbInteger, {Integer a, Integer b ->
			Property.prop(a + b == b + a)
		} as F2)
		def cr = p.check()
		cr.printlnSummary()
		assertTrue(cr.isOk())
	}

	/**
	 * Sometimes the number of tests discarded does not get printed.  This test shows how
	 * to work around that
	 */
	@Test
	void implies2() {
		def p = Property.property(Arbitrary.arbInteger, Arbitrary.arbInteger, {Integer a, Integer b ->
			def preOk = (a >= 0 && b >= 0)
			// executing sum2 seems to make it lose discarded results, so do this lazily
			def t = !preOk ? true : (a + b == b + a)
			Bool.bool(preOk).implies(t)
		} as F2)
		p.checkOkWithSummary()
	}

	@Test
	void impliesHandlingNulls() {
		def arb = Arbitrary.arbitrary(Gen.oneOf(fj.data.List.list([Gen.value(null), Arbitrary.arbInteger.gen].toArray())))
		def p = Property.property(arb, Arbitrary.arbInteger, { Integer a, Integer b ->
			try {
				def preOk = (a != null && b != null)
//				def preOk = true
				def t = !preOk ? true : (a + b == b + a)
				Bool.bool(preOk).implies(t)
			} catch (Exception e) {
				Property.prop(false)
			}
		} as F2)
		p.checkBooleanWithNullableSummary(true)
	}

}
