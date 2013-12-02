package com.github.mperry.fg.test

import fj.F
import fj.F2
import fj.P1
import fj.data.Option
import fj.data.Validation
import fj.test.Arbitrary
import groovy.transform.TypeChecked
import org.junit.Test

import static com.github.mperry.fg.test.PropertyTester.NULLABLE_INTEGER
import static com.github.mperry.fg.test.PropertyTester.defaultMap
import static com.github.mperry.fg.test.PropertyTester.showAll
import static com.github.mperry.fg.test.TestConfig.validator
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

	@Test
	void impliesHandlingNulls5() {
		showAll new TestConfig(
			map: [(Integer.class): Arbitrary.arbNullableInteger()],
			function: { Integer a, Integer b ->
				a + b == b + a
			},
			validator: validator({ Throwable t -> t.getClass() == NullPointerException.class } as F)
		)
	}

	@Test
	void test5() {
		def f = { Integer a -> a }
		def f2 = { Integer a -> a } as F
		def f3 = f as F
		def f4 = new F<Integer, Integer>(){
			@Override
			Integer f(Integer a) {
				a
			}
		}
		def f5 = f4.toClosure()


		def a1 = [f, f5].collect { it.getParameterTypes() }
		def a2 = [f2, f3, f4].collect {it.f(3)}

		int z = 0
	}

}
