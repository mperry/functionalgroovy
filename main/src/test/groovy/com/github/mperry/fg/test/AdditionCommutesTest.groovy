
package com.github.mperry.fg.test

import fj.F
import fj.test.Arbitrary
import groovy.transform.TypeChecked
import org.junit.Test

import static Model.validator
import static com.github.mperry.fg.test.Specification.specAssert
import static fj.data.Option.some

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 9/08/13
 * Time: 1:07 AM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class AdditionCommutesTest {

	@Test
	void commutes() {
		specAssert { Integer a, Integer b ->
			a + b == b + a
		}
	}

//	@Test
	void subtractionDoesNotCommute1() {
		specAssert { Integer a, Integer b ->
				a - b == b - a
		}
	}

	@Test
	void subtractionDoesNotCommute2() {
		specAssert new Model(
				function: { Integer a, Integer b ->
					a - b == b - a
				},
				truth: false
		)
	}

	/**
	 * Naturals commute, do not discard generated values that are not naturals
	 */
	@Test
	void naturalsCommute() {
		specAssert { Integer a, Integer b ->
			(a >= 0 && b >= 0).implies(a + b == b + a)
		}
	}

	/**
	 * Naturals commute, discard values that are not naturals
	 */
	@Test
	void naturalsCommuteDiscardInvalid() {
		specAssert new Model(
			pre: some { Integer a, Integer b -> a >= 0 && b >= 0 },
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
		specAssert(new Model(
			map: Model.DEFAULT_MAP + Model.NULLABLE_INTEGER,
			pre: some { Integer a, Integer b -> a != null && b != null },
			function: { Integer a, Integer b ->
				a + b == b + a
			}
		))
	}

	/**
	 * Show property does not hold that naturals commute in the presence of null values
	 */
	@Test
	void integersWithNullsDoNotCommute() {
		specAssert new Model(
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
	void integersCommuteIfNullPointerOk() {
		specAssert new Model(
			map: [(Integer.class): Arbitrary.arbNullableInteger()],
			function: { Integer a, Integer b ->
				a + b == b + a
			},
			validator: validator({ Throwable t -> t.isDirectInstanceOf(NullPointerException.class) } as F)
		)
	}

	/**
	 * Naturals commute where a null argument produces a NullPointerException
	 */
	@Test
	void integersCommuteWithNullPointer() {
		specAssert new Model(
				map: [(Integer.class): Arbitrary.arbNullableInteger()],
				function: { Integer a, Integer b ->
					try {
						a + b == b + a
					} catch (NullPointerException e) {
						(a == null || b == null)
					}
				}
		)
	}


}
