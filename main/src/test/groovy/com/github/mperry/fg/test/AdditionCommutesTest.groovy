package com.github.mperry.fg.test

import fj.F
import fj.test.Arbitrary
import org.junit.Test

import static com.github.mperry.fg.test.PropertyTester.showAll
import static PropertyConfig.validator
import static fj.data.Option.some

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 9/08/13
 * Time: 1:07 AM
 * To change this template use File | Settings | File Templates.
 */
class AdditionCommutesTest {

	@Test
	void commutes() {
		showAll { Integer a, Integer b ->
			a + b == b + a
		}
	}

	/**
	 * Naturals commute, do not discard generated values that are not naturals
	 */
	@Test
	void naturalsCommute() {
		showAll { Integer a, Integer b ->
			(a >= 0 && b >= 0).implies(a + b == b + a)
		}
	}

	/**
	 * Naturals commute, discard values that are not naturals
	 */
	@Test
	void naturalsCommuteDiscardInvalid() {
		showAll new PropertyConfig(
			pre: some { a, b -> a >= 0 && b >= 0 },
			function: { Integer a, Integer b ->
				a + b == b + a
			}
		)
	}

	/**
	 * Naturals commute, enhance pre-condition to check for null values
	 */
	@Test
	void discardNulls() {
		showAll(new PropertyConfig(
			map: PropertyConfig.DEFAULT_MAP + PropertyConfig.NULLABLE_INTEGER,
			pre: some({ a, b -> a != null && b != null }),
			function: { Integer a, Integer b ->
				a + b == b + a
			}
		))
	}

	/**
	 * Show property does not hold that naturals commute in the presence of null values
	 */
	@Test
	void naturalsWithNullsDoNotCommute() {
		showAll new PropertyConfig(
			truth: false,
			map: [(Integer.class): Arbitrary.arbNullableInteger()],
			function: { Integer a, Integer b ->
				a + b == b + a
			}
		)
	}

	/**
	 * Naturals commute if throwing a NullPointerException is ok
	 */
	@Test
	void naturalsCommuteIfNullPointerOk() {
		showAll new PropertyConfig(
			map: [(Integer.class): Arbitrary.arbNullableInteger()],
			function: { Integer a, Integer b ->
				a + b == b + a
			},
			validator: validator({ Throwable t -> t.getClass() == NullPointerException.class } as F)
		)
	}

}
