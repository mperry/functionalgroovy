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
	void commutes() {
		def p = Property.property(Arbitrary.arbInteger, Arbitrary.arbInteger, {Integer a, Integer b ->
			Property.prop(a + b == b + a)
		} as F2)
		p.checkOkWithSummary()
	}

	@Test
	void naturalsCommute() {
		def p = Property.property(Arbitrary.arbInteger, Arbitrary.arbInteger, {Integer a, Integer b ->
			Bool.bool(a >= 0 && b >= 0).implies(a + b == b + a)
		} as F2)
		p.checkOkWithSummary()
	}

	/**
	 * Sometimes the number of tests discarded does not get printed.  This test shows how
	 * to work around that
	 */
	@Test
	void naturalsCommute2() {
		def p = Property.property(Arbitrary.arbInteger, Arbitrary.arbInteger, {Integer a, Integer b ->
			def preOk = (a >= 0 && b >= 0)
			// executing sum2 seems to make it lose discarded results, so do this lazily
			def t = !preOk ? true : (a + b == b + a)
			Bool.bool(preOk).implies(t)
		} as F2)
		p.checkOkWithSummary()
	}

	@Test
	void naturalsCommute3() {
		def p = Property.property(Arbitrary.arbInteger, Arbitrary.arbInteger, {Integer a, Integer b ->
			propWithPre2({c, d -> c >= 0 && d >= 0} as F2, {c, d -> c + d == d + c} as F2, a, b)
		} as F2)
		p.checkOkWithSummary()
	}

	static <A, B> Property propWithPre2(F2<A, B, Boolean> pre, F2<A, B, Boolean> post, A arg1, B arg2) {
		try {
			def preOk = pre.f(arg1, arg2)
			def b = !preOk ? true : post.f(arg1, arg2)
			Bool.bool(preOk).implies(b)
		} catch (Exception e) {
			Property.prop(false)
		}
	}

	@Test
	void impliesHandlingNulls() {
		def arb = Arbitrary.arbitrary(Gen.oneOf(fj.data.List.list([Gen.value(null), Arbitrary.arbInteger.gen].toArray())))
		def p = Property.property(arb, Arbitrary.arbInteger, { Integer a, Integer b ->
			try {
				def preOk = (a != null && b != null)
				def t = !preOk ? true : (a + b == b + a)
				Bool.bool(preOk).implies(t)
			} catch (Exception e) {
				Property.prop(false)
			}
		} as F2)
		p.checkOkWithSummary()
	}

}
