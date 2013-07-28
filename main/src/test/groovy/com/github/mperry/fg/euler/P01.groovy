package com.github.mperry.fg.euler

import groovy.transform.TypeChecked
import junit.framework.TestCase
import org.junit.Test

/*
 * If we list all the natural numbers below 10 that are multiples of 3 or 5,
 * we get 3, 5, 6 and 9. The sum of these multiples is 23.
 *
 * Find the sum of all the multiples of 3 or 5 below 1000.
 *
 */
@TypeChecked
class P01 extends GroovyTestCase {

	def f(int min, int max) {
		min.to(max).filter { Integer it ->
			it % 3 == 0 || it % 5 == 0
		}.fold(0) { Integer acc, Integer val ->
			acc + val
		}
	}

	@Test
	void test1() {
		assertTrue(f(1, 9) == 23)
		assertTrue(f(1, 10 ** 3 - 1) == 233168)
	}

}
