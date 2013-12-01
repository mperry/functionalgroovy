package com.github.mperry.fg.test

import fj.F2
import fj.Show
import fj.data.Option
import fj.test.Arbitrary
import fj.test.Bool
import fj.test.Gen
import fj.test.Property
import org.junit.Test

import static com.github.mperry.fg.test.PropertyTester.NULLABLE_INTEGER
import static com.github.mperry.fg.test.PropertyTester.defaultMap
import static com.github.mperry.fg.test.PropertyTester.showAll
import static fj.data.Option.some
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
		showAll new TestConfig(
			pre: Option.some { a, b -> a >= 0 && b >= 0 },
			function: { Integer a, Integer b ->
				a + b == b + a
			}
		)
	}

	@Test
	void impliesHandlingNulls1() {
		showAll(new TestConfig(
			map: defaultMap + NULLABLE_INTEGER,
			pre: some({ a, b -> a != null && b != null }),
			function: { Integer a, Integer b ->
				a + b == b + a
			}
		))
//		showAll
//		showAll([(Integer.class): Arbitrary.arbNullableInteger()]) { a, b -> a != null && b != null } { Integer a, Integer b ->
//			a + b == b + a
//		}
	}

	@Test
	void impliesHandlingNulls2() {
		showAll new TestConfig(
			truth: false,
			map: [(Integer.class): Arbitrary.arbNullableInteger()],
			function: { Integer a, Integer b ->
				a + b == b + a
			}
		)
	}

}
