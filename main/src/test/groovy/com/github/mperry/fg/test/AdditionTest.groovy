package com.github.mperry.fg.test

import fj.F
import fj.data.Option
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
		showAll new PropertyConfig(
			pre: Option.some { a, b -> a >= 0 && b >= 0 },
			function: { Integer a, Integer b ->
				a + b == b + a
			}
		)
	}

	@Test
	void impliesHandlingNulls1() {
		showAll(new PropertyConfig(
			map: PropertyConfig.defaultMap + PropertyConfig.NULLABLE_INTEGER,
			pre: some({ a, b -> a != null && b != null }),
			function: { Integer a, Integer b ->
				a + b == b + a
			}
		))
	}

	@Test
	void impliesHandlingNulls2() {
		showAll new PropertyConfig(
				truth: false,
				map: [(Integer.class): Arbitrary.arbNullableInteger()],
				function: { Integer a, Integer b ->
					a + b == b + a
				}

		)
	}

	@Test
	void impliesHandlingNulls5() {
		showAll new PropertyConfig(
			map: [(Integer.class): Arbitrary.arbNullableInteger()],
			function: { Integer a, Integer b ->
				a + b == b + a
			},
			validator: validator({ Throwable t -> t.getClass() == NullPointerException.class } as F)
		)
	}

}
