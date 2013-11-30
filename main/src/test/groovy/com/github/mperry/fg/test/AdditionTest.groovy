package com.github.mperry.fg.test

import fj.F2
import fj.Show
import fj.test.Arbitrary
import fj.test.Bool
import fj.test.Gen
import fj.test.Property
import org.junit.Test

import static com.github.mperry.fg.test.PropertyTester.showAll
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
		showAll { Integer a, Integer b ->
			a + b == b + a
		}
	}

	@Test
	void naturalsCommute() {
		showAll { Integer a, Integer b ->
			(a >= 0 && b >= 0).implies(a + b == b + a)
		}
	}

	/**
	 * Sometimes the number of tests discarded does not get printed.  This test shows how
	 * to work around that
	 */
	@Test
	void naturalsCommute2() {
		showAll { a, b -> a >= 0 && b >= 0} { Integer a, Integer b ->
			a + b == b + a
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

//	static final Arbitrary<Integer> arbNullableInteger = Arbitrary.arbitrary(Gen.oneOf(fj.data.List.list([Gen.value(null), Arbitrary.arbInteger.gen].toArray())))
//	static final Arbitrary<Integer> arbNullableInteger = Arbitrary.arbNullableInteger()

	@Test
	void impliesHandlingNulls2() {
		showAll([(Integer.class): Arbitrary.arbNullableInteger()]) { a, b -> a != null && b != null } { Integer a, Integer b ->
			a + b == b + a
		}
	}

	@Test
	void impliesHandlingNulls3() {
		showAll([(Integer.class): Arbitrary.arbNullableInteger()]) { Integer a, Integer b ->
			a + b == b + a
		}
	}

}
